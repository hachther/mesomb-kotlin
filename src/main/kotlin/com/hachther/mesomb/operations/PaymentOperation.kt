package com.hachther.mesomb.operations

import com.hachther.mesomb.MeSomb
import com.hachther.mesomb.Signature
import com.hachther.mesomb.exceptions.InvalidClientRequestException
import com.hachther.mesomb.exceptions.PermissionDeniedException
import com.hachther.mesomb.exceptions.ServerException
import com.hachther.mesomb.exceptions.ServiceNotFoundException
import com.hachther.mesomb.models.Application
import com.hachther.mesomb.models.TransactionResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.net.MalformedURLException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.text.ParseException
import java.util.*
import java.util.concurrent.TimeUnit


class PaymentOperation(
    private var applicationKey: String,
    private var accessKey: String,
    private var secretKey: String
) {
    companion object {
        val MEDIA_TYPE_JSON = "application/json; charset=utf-8".toMediaType()
    }

    private fun buildUrl(endpoint: String): String {
        return "${MeSomb.apiBase}/${MeSomb.language}/api/${MeSomb.apiVersion}/$endpoint"
    }

    @Throws(
        MalformedURLException::class,
        UnsupportedEncodingException::class,
        NoSuchAlgorithmException::class,
        InvalidKeyException::class
    )
    private fun getAuthorization(
        method: String,
        endpoint: String,
        date: Date,
        nonce: String,
        headers: TreeMap<String, String>?,
        body: Map<String, Any>?
    ): String {
        val url = buildUrl(endpoint)
        val credentials: MutableMap<String, String?> = HashMap()
        credentials["accessKey"] = accessKey
        credentials["secretKey"] = secretKey
        return Signature.signRequest("payment", method, url, date, nonce, credentials, headers, body)
    }

    @Throws(
        MalformedURLException::class,
        UnsupportedEncodingException::class,
        NoSuchAlgorithmException::class,
        InvalidKeyException::class
    )
    private fun getAuthorization(method: String, endpoint: String, date: Date, nonce: String): String {
        return this.getAuthorization(method, endpoint, date, nonce, null, null)
    }

    @Throws(
        IOException::class,
        ServiceNotFoundException::class,
        PermissionDeniedException::class,
        InvalidClientRequestException::class,
        ServerException::class
    )
    private fun processClientException(statusCode: Int, response: String) {
        var code: String? = null
        var message = response
        if (message.startsWith("{")) {
            val parser = JSONParser()
            try {
                val data = parser.parse(message) as JSONObject
                message = data["detail"] as String
                code = data["code"] as String?
            } catch (ignored: ParseException) {
            }
        }
        when (statusCode) {
            404 -> throw ServiceNotFoundException(message)
            403, 401 -> throw PermissionDeniedException(message)
            400 -> throw InvalidClientRequestException(message, code!!)
            else -> throw ServerException(message, code)
        }
    }

    @Throws(
        IOException::class,
        MalformedURLException::class,
        UnsupportedEncodingException::class,
        NoSuchAlgorithmException::class,
        InvalidKeyException::class,
        InvalidClientRequestException::class,
        ServerException::class,
        ServiceNotFoundException::class,
        PermissionDeniedException::class
    )
    private fun executeRequest(
        method: String,
        endpoint: String,
        date: Date,
        nonce: String = "",
        body: MutableMap<String, Any>? = null,
        mode: String? = null
    ): String {
        val url = buildUrl(endpoint)
        var trxID: String? = null
        if (body != null && body.containsKey("trxID")) {
            trxID = body["trxID"] as String?
            body.remove("trxID")
        }
        val authorization: String = if (method == "POST") {
            val headers = TreeMap<String, String>()
            headers["content-type"] = MEDIA_TYPE_JSON.toString()
            body?.set("source", "MeSombKotlin/" + MeSomb.version)
            this.getAuthorization(method, endpoint, date, nonce, headers, body)
        } else {
            this.getAuthorization(method, endpoint, date, nonce)
        }
        val client: OkHttpClient = OkHttpClient.Builder().readTimeout(30, TimeUnit.SECONDS).build()
        var builder: Request.Builder = Request.Builder()
            .url(url)
            .method(method, if (body != null) JSONObject.toJSONString(body).toRequestBody(MEDIA_TYPE_JSON) else null)
            .addHeader("x-mesomb-date", java.lang.String.valueOf(date.time / 1000))
            .addHeader("x-mesomb-nonce", nonce)
            .addHeader("Authorization", authorization)
            .addHeader("X-MeSomb-Application", applicationKey)
        if (mode != null) {
            builder = builder.addHeader("X-MeSomb-OperationMode", mode)
        }
        if (trxID != null) {
            builder = builder.addHeader("X-MeSomb-TrxID", trxID)
        }
        client.newCall(builder.build()).execute().use { response ->
            if (response.code >= 400) {
                assert(response.body != null)
                processClientException(response.code, response.body!!.string())
            }
            assert(response.body != null)
            return response.body!!.string()
        }
    }

    /**
     * Collect money a user account
     *
     * @param params object with the below information
     *               - amount: amount to collect
     *               - service: payment service with the possible values MTN, ORANGE, AIRTEL
     *               - payer: account number to collect from
     *               - date: date of the request
     *               - nonce: unique string on each request
     *               - country: 2 letters country code of the service (configured during your service registration in MeSomb)
     *               - currency: currency of your service depending on your country
     *               - fees: false if your want MeSomb fees to be computed and included in the amount to collect
     *               - mode: asynchronous or synchronous
     *               - conversion: true in case of foreign currently defined if you want to rely on MeSomb to convert the amount in the local currency
     *               - location: Map containing the location of the customer with the following attributes: town, region and location all string.
     *               - products: It is ArrayList of products. Each product are Map with the following attributes: name string, category string, quantity int and amount float
     *               - customer: a Map containing information about the customer: phone string, email: string, first_name string, last_name string, address string, town string, region string and country string
     *               - trxID: if you want to include your transaction ID in the request
     *               - extra: Map to add some extra attribute depending on the API documentation
     * @return TransactionResponse
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws ServerException
     * @throws ServiceNotFoundException
     * @throws PermissionDeniedException
     * @throws InvalidClientRequestException
     */
    @Throws(
        IOException::class,
        NoSuchAlgorithmException::class,
        InvalidKeyException::class,
        ServerException::class,
        ServiceNotFoundException::class,
        PermissionDeniedException::class,
        InvalidClientRequestException::class
    )
    fun makeCollect(
        params: Map<String, Any?>
    ): TransactionResponse? {
        val endpoint = "payment/collect/"
        val body: MutableMap<String, Any> = HashMap()

        val mode: String = params.getOrDefault("mode", "synchronous") as String
        val date: Date = params.getOrDefault("date", Date()) as Date
        val nonce: String = params["nonce"] as String

        body["amount"] = params["amount"] as Float
        body["service"] = params["service"] as String
        body["payer"] = params["payer"] as String
        body["country"] = params.getOrDefault("country", "CM") as String
        body["currency"] = params.getOrDefault("currency", "XAF") as String
        body["fees"] = params.getOrDefault("fees", true) as Boolean
        body["conversion"] = params.getOrDefault("conversion", false) as Boolean
        if (params.getOrDefault("trxID", null) != null) {
            body["trxID"] = params["trxID"] as String
        }
        if (params.getOrDefault("location", null) != null) {
            body["location"] = params["location"] as Map<String, String?>
        }
        if (params.getOrDefault("customer", null) != null) {
            body["customer"] = params["customer"] as Map<String, Any?>
        }
        if (params.getOrDefault("products", null) != null) {
            body["products"] = params["products"] as ArrayList<Map<String, Any?>>
        }
        if (params.containsKey("extra")) {
            val extra = params["extra"] as Map<String, Any?>
            for (key in extra.keys) {
                body[key] = extra[key] as String
            }
        }
        val parser = JSONParser()
        return try {
            TransactionResponse(
                parser.parse(this.executeRequest(
                    "POST",
                    endpoint,
                    date,
                    nonce,
                    body,
                    mode
                )) as JSONObject
            )
        } catch (e: ParseException) {
            throw ServerException("Issue to parse transaction response", "parsing-issue")
        }
    }

    /**
     * Method to make deposit in a receiver mobile account.
     *
     * @param params object with the below information
     *               - amount: amount to collect
     *               - service: payment service with the possible values MTN, ORANGE, AIRTEL
     *               - receiver: account number to depose money
     *               - date: date of the request
     *               - nonce: unique string on each request
     *               - country: 2 letters country code of the service (configured during your service registration in MeSomb)
     *               - currency: currency of your service depending on your country
     *               - conversion: true in case of foreign currently defined if you want to rely on MeSomb to convert the amount in the local currency
     *               - location: Map containing the location of the customer with the following attributes: town, region and location all string.
     *               - products: It is array of products. Each product are Map with the following attributes: name string, category string, quantity int and amount float
     *               - customer: a Map containing information about the customer: phone string, email: string, first_name string, last_name string, address string, town string, region string and country string
     *               - trxID: if you want to include your transaction ID in the request
     *               - extra: Map to add some extra attribute depending on the API documentation
     * @return TransactionResponse
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws ServerException
     * @throws ServiceNotFoundException
     * @throws PermissionDeniedException
     * @throws InvalidClientRequestException
     */
    @Throws(
        IOException::class,
        NoSuchAlgorithmException::class,
        InvalidKeyException::class,
        ServerException::class,
        ServiceNotFoundException::class,
        PermissionDeniedException::class,
        InvalidClientRequestException::class
    )
    fun makeDeposit(
        params: Map<String, Any?>
    ): TransactionResponse? {
        val endpoint = "payment/deposit/"
        val body: MutableMap<String, Any> = HashMap()

        val date: Date = params.getOrDefault("date", Date()) as Date
        val nonce: String = params["nonce"] as String

        body["amount"] = params["amount"] as Float
        body["service"] = params["service"] as String
        body["receiver"] = params["receiver"] as String
        body["country"] = params.getOrDefault("country", "CM") as String
        body["currency"] = params.getOrDefault("currency", "XAF") as String
        if (params.getOrDefault("trxID", null) != null) {
            body["trxID"] = params["trxID"] as String
        }
        if (params.getOrDefault("location", null) != null) {
            body["location"] = params["location"] as Map<String, String?>
        }
        if (params.getOrDefault("customer", null) != null) {
            body["customer"] = params["customer"] as Map<String, Any?>
        }
        if (params.getOrDefault("products", null) != null) {
            body["products"] = params["products"] as Array<Map<String, Any?>>
        }
        if (params.containsKey("extra")) {
            val extra = params["extra"] as Map<String, Any?>
            for (key in extra.keys) {
                body[key] = extra[key] as String
            }
        }
        val parser = JSONParser()
        return try {
            TransactionResponse(
                parser.parse(
                    this.executeRequest(
                        "POST",
                        endpoint,
                        date,
                        nonce,
                        body
                    )
                ) as JSONObject
            )
        } catch (e: ParseException) {
            throw ServerException("Issue to parse transaction response", "parsing-issue")
        }
    }

    /**
     * Update security parameters of your service on MeSomb
     *
     * @param field which security field you want to update (check API documentation)
     * @param action action SET or UNSET
     * @param value value of the field
     * @param date date of the request
     * @return Application
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws ServerException
     * @throws ServiceNotFoundException
     * @throws PermissionDeniedException
     * @throws InvalidClientRequestException
     */
    @Throws(
        IOException::class,
        NoSuchAlgorithmException::class,
        InvalidKeyException::class,
        ServerException::class,
        ServiceNotFoundException::class,
        PermissionDeniedException::class,
        InvalidClientRequestException::class
    )
    fun updateSecurity(field: String, action: String, value: Any? = null, date: Date? = null): Application {
        var date = date
        val endpoint = "payment/security/"
        val body: MutableMap<String, Any> = HashMap()
        body["field"] = field
        body["action"] = action
        if (action != "UNSET" && value != null) {
            body["value"] = value
        }
        if (date == null) {
            date = Date()
        }
        val parser = JSONParser()
        return try {
            Application(parser.parse(this.executeRequest("POST", endpoint, date, "", body)) as JSONObject)
        } catch (e: ParseException) {
            throw ServerException("Issue to parse transaction response", "parsing-issue")
        }
    }

    /**
     * Get the current status of your service on MeSomb
     *
     * @param date date of the request
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws ServerException
     * @throws ServiceNotFoundException
     * @throws PermissionDeniedException
     * @throws InvalidClientRequestException
     */
    @Throws(
        IOException::class,
        NoSuchAlgorithmException::class,
        InvalidKeyException::class,
        ServerException::class,
        ServiceNotFoundException::class,
        PermissionDeniedException::class,
        InvalidClientRequestException::class
    )
    fun getStatus(): Application? {
        val endpoint = "payment/status/"
        val parser = JSONParser()
        return try {
            Application(parser.parse(this.executeRequest("GET", endpoint, Date())) as JSONObject)
        } catch (e: ParseException) {
            throw ServerException("Issue to parse transaction response", "parsing-issue")
        }
    }

    /**
     * Get transactions stored in MeSomb based on the list
     *
     * @param ids Ids of transactions to fetch
     * @param date date to consider in the request
     * @return List of the transactions fetched
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws ServerException
     * @throws ServiceNotFoundException
     * @throws PermissionDeniedException
     * @throws InvalidClientRequestException
     */
    @Throws(
        IOException::class,
        NoSuchAlgorithmException::class,
        InvalidKeyException::class,
        ServerException::class,
        ServiceNotFoundException::class,
        PermissionDeniedException::class,
        InvalidClientRequestException::class
    )
    fun getTransactions(ids: Array<String>, source: String? = "MESOMB"): JSONArray? {
        val endpoint = "payment/transactions/?ids=" + java.lang.String.join(",", *ids) + "&source=$source"
        val parser = JSONParser()
        return try {
            parser.parse(this.executeRequest("GET", endpoint, Date())) as JSONArray
        } catch (e: ParseException) {
            throw ServerException("Issue to parse transaction response", "parsing-issue")
        }
    }

    /**
     * Reprocess transaction at the operators level to confirm the status of a transaction
     * @param ids list of transaction ids
     * @param date date to consider in the request
     * @return List of the transactions processed
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws ServerException
     * @throws ServiceNotFoundException
     * @throws PermissionDeniedException
     * @throws InvalidClientRequestException
     */
    @Throws(
        IOException::class,
        NoSuchAlgorithmException::class,
        InvalidKeyException::class,
        ServerException::class,
        ServiceNotFoundException::class,
        PermissionDeniedException::class,
        InvalidClientRequestException::class
    )
    fun checkTransactions(ids: Array<String>, source: String? = "MESOMB"): JSONArray? {
        val endpoint = "payment/transactions/check/?ids=" + java.lang.String.join(",", *ids) + "&source=$source"
        val parser = JSONParser()
        return try {
            parser.parse(this.executeRequest("GET", endpoint, Date())) as JSONArray
        } catch (e: ParseException) {
            throw ServerException("Issue to parse transaction response", "parsing-issue")
        }
    }
}