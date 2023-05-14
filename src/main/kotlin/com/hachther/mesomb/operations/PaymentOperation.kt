package com.hachther.mesomb.operations

import com.hachther.mesomb.MeSomb
import com.hachther.mesomb.Signature
import com.hachther.mesomb.exceptions.InvalidClientRequestException
import com.hachther.mesomb.exceptions.PermissionDeniedException
import com.hachther.mesomb.exceptions.ServerException
import com.hachther.mesomb.exceptions.ServiceNotFoundException
import com.hachther.mesomb.models.Application
import com.hachther.mesomb.models.TransactionResponse
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
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
        return "${MeSomb.apiBase}/en/api/${MeSomb.apiVersion}/$endpoint"
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
        body: Map<String, Any>? = null,
        mode: String? = null
    ): String? {
        val url = buildUrl(endpoint)
        val authorization: String = if (method == "POST") {
            val headers = TreeMap<String, String>()
            headers["content-type"] = MEDIA_TYPE_JSON.toString()
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
     * @param amount       amount to collect
     * @param service      MTN, ORANGE, AIRTEL
     * @param payer        account number to collect from
     * @param date         date of the request
     * @param nonce        unique string on each request
     * @param country      country CM, NE
     * @param currency     code of the currency of the amount
     * @param feesIncluded if your want MeSomb to include and compute fees in the amount to collect
     * @param mode         asynchronous or synchronous
     * @param conversion   In case of foreign currently defined if you want to rely on MeSomb to convert the amount in the local currency
     * @param location     Map containing the location of the customer check the documentation
     * @param customer     Map containing information of the customer check the documentation
     * @param product      Map containing information of the product check the documentation
     * @param extra        Extra parameter to send in the body check the API documentation
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
        amount: Float,
        service: String,
        payer: String,
        date: Date = Date(),
        nonce: String?,
        country: String? = "CM",
        currency: String? = "XAF",
        feesIncluded: Boolean = true,
        mode: String? = "synchronous",
        conversion: Boolean = false,
        location: Map<String, String?>? = null,
        customer: Map<String, String?>? = null,
        product: Map<String, String?>? = null,
        extra: Map<String, Any?>? = null
    ): TransactionResponse? {
        val endpoint = "payment/collect/"
        val body: MutableMap<String, Any> = HashMap()
        body["amount"] = amount
        body["service"] = service
        body["payer"] = payer
        body["country"] = country as String
        body["currency"] = currency as String
        body["fees"] = feesIncluded
        body["conversion"] = conversion
        if (location != null) {
            body["location"] = location
        }
        if (customer != null) {
            body["customer"] = customer
        }
        if (product != null) {
            body["product"] = product
        }
        if (extra != null) {
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
                    nonce!!,
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
     * @param amount the amount of the transaction
     * @param service service code (MTN, ORANGE, AIRTEL, ...)
     * @param receiver receiver account (in the local phone number)
     * @param date date of the request
     * @param nonce Unique key generated for each transaction
     * @param country country code, 'CM' by default
     * @param currency currency of the transaction (XAF, XOF, ...) XAF by default
     * @param extra extra parameters to send in the body check the API documentation
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
        amount: Float,
        service: String,
        receiver: String,
        date: Date? = Date(),
        nonce: String?,
        country: String? = "CM",
        currency: String? = "XAF",
        extra: Map<String, Any>? = null
    ): TransactionResponse? {
        val endpoint = "payment/deposit/"
        val body: MutableMap<String, Any> = HashMap()
        body["amount"] = amount
        body["service"] = service
        body["receiver"] = receiver
        body["country"] = country as String
        body["currency"] = currency as String
        if (extra != null) {
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
                        date!!,
                        nonce!!,
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
    fun getStatus(date: Date? = Date()): Application? {
        val endpoint = "payment/status/"
        val parser = JSONParser()
        return try {
            Application(parser.parse(date?.let { this.executeRequest("GET", endpoint, it) }) as JSONObject)
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
    fun getTransactions(ids: Array<String>, date: Date? = Date()): JSONArray? {
        val endpoint = "payment/transactions/?ids=" + java.lang.String.join(",", *ids)
        val parser = JSONParser()
        return try {
            parser.parse(date?.let { this.executeRequest("GET", endpoint, it) }) as JSONArray
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
    fun checkTransactions(ids: Array<String>, date: Date? = Date()): JSONArray? {
        val endpoint = "payment/transactions/check/?ids=" + java.lang.String.join(",", *ids)
        val parser = JSONParser()
        return try {
            parser.parse(date?.let { this.executeRequest("GET", endpoint, it) }) as JSONArray
        } catch (e: ParseException) {
            throw ServerException("Issue to parse transaction response", "parsing-issue")
        }
    }
}