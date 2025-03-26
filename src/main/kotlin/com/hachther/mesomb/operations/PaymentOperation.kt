package com.hachther.mesomb.operations

import com.hachther.mesomb.exceptions.InvalidClientRequestException
import com.hachther.mesomb.exceptions.PermissionDeniedException
import com.hachther.mesomb.exceptions.ServerException
import com.hachther.mesomb.exceptions.ServiceNotFoundException
import com.hachther.mesomb.models.Application
import com.hachther.mesomb.models.Transaction
import com.hachther.mesomb.models.TransactionResponse
import com.hachther.mesomb.util.RandomGenerator.nonce
import org.json.JSONObject
import org.json.JSONArray
import java.io.IOException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.text.ParseException
import java.util.Date


class PaymentOperation(applicationKey: String, accessKey: String, secretKey: String, language: String = "en") : AOperation(applicationKey, accessKey, secretKey, language) {
    /**
     * Method to make a payment
     *
     * @param amount amount to collect
     * @param service payment service with the possible values MTN, ORANGE, AIRTEL
     * @param payer Account number to collect from
     * @param nonce unique string on each request
     * @param country 2 letters country code of the service (configured during your service registration in MeSomb)
     * @param currency currency of your service depending on your country
     * @param fees true if you want to include the fees in the amount
     * @param conversion true in case of foreign currently defined if you want to rely on MeSomb to convert the amount in the local currency
     * @param mode synchronous or asynchronous
     * @param location Map containing the location of the customer with the following attributes: town, region and location all string.
     * @param products It is array of products. Each product are Map with the following attributes: name string, category string, quantity int and amount float
     * @param customer a Map containing information about the customer: phone string, email: string, first_name string, last_name string, address string, town string, region string and country string
     * @param trxID if you want to include your transaction ID in the request
     *
     * @return TransactionResponse
     *
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
        amount: Double,
        service: String,
        payer: String,
        nonce: String? = null,
        country: String = "CM",
        currency: String = "XAF",
        fees: Boolean = true,
        conversion: Boolean = false,
        mode: String = "synchronous",
        location: Map<String, String>? = null,
        products: List<Map<String, String>>? = null,
        customer: Map<String, String>? = null,
        trxID: String? = null,
    ): TransactionResponse {
        val endpoint = "payment/collect/"

        val body: MutableMap<String, Any> = mapOf(
            "amount" to amount,
            "service" to service,
            "payer" to payer,
            "country" to country,
            "currency" to currency,
            "fees" to fees,
            "conversion" to conversion,
        ).toMutableMap()
        if (trxID != null) {
            body["trxID"] = trxID
        }
        if (location != null) {
            body["location"] = location
        }
        if (customer != null) {
            body["customer"] = customer
        }
        if (products != null) {
            body["products"] = products
        }

        return TransactionResponse(JSONObject(this.executeRequest(
            "POST",
            endpoint,
            Date(),
            nonce ?: nonce(),
            body,
            mode
        )))
    }

    /**
     * Method to make a payment
     *
     * @param amount amount to collect
     * @param service payment service with the possible values MTN, ORANGE, AIRTEL
     * @param receiver Account number to depose money to
     * @param nonce unique string on each request
     * @param country 2 letters country code of the service (configured during your service registration in MeSomb)
     * @param currency currency of your service depending on your country
     * @param conversion true in case of foreign currently defined if you want to rely on MeSomb to convert the amount in the local currency
     * @param mode synchronous or asynchronous
     * @param location Map containing the location of the customer with the following attributes: town, region and location all string.
     * @param products It is array of products. Each product are Map with the following attributes: name string, category string, quantity int and amount float
     * @param customer a Map containing information about the customer: phone string, email: string, first_name string, last_name string, address string, town string, region string and country string
     * @param trxID if you want to include your transaction ID in the request
     *
     * @return TransactionResponse
     *
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
        amount: Double,
        service: String,
        receiver: String,
        nonce: String? = null,
        country: String = "CM",
        currency: String = "XAF",
        conversion: Boolean = false,
        mode: String = "synchronous",
        location: Map<String, String>? = null,
        products: ArrayList<Map<String, Any>>? = null,
        customer: Map<String, String>? = null,
        trxID: String? = null,
    ): TransactionResponse {
        val endpoint = "payment/deposit/"

        val body: MutableMap<String, Any> = mapOf(
            "amount" to amount,
            "service" to service,
            "receiver" to receiver,
            "country" to country,
            "currency" to currency,
            "conversion" to conversion,
        ).toMutableMap()
        if (trxID != null) {
            body["trxID"] = trxID
        }
        if (location != null) {
            body["location"] = location
        }
        if (customer != null) {
            body["customer"] = customer
        }
        if (products != null) {
            body["products"] = products
        }

        return TransactionResponse(
            JSONObject(this.executeRequest(
                "POST",
                endpoint,
                Date(),
                nonce ?: nonce(),
                body
            ))
        )
    }

    /**
     * Method to make a payment
     *
     * @param amount amount to collect
     * @param service payment service with the possible values MTN, ORANGE, AIRTEL
     * @param receiver Account number to depose money to
     * @param merchant merchant of the service
     * @param nonce unique string on each request
     * @param country 2 letters country code of the service (configured during your service registration in MeSomb)
     * @param currency currency of your service depending on your country
     * @param conversion true in case of foreign currently defined if you want to rely on MeSomb to convert the amount in the local currency
     * @param mode synchronous or asynchronous
     * @param location Map containing the location of the customer with the following attributes: town, region and location all string.
     * @param products It is array of products. Each product are Map with the following attributes: name string, category string, quantity int and amount float
     * @param customer a Map containing information about the customer: phone string, email: string, first_name string, last_name string, address string, town string, region string and country string
     * @param trxID if you want to include your transaction ID in the request
     *
     * @return TransactionResponse
     *
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
    fun purchaseAirtime(
        amount: Double,
        service: String,
        receiver: String,
        merchant: String,
        nonce: String? = null,
        country: String = "CM",
        currency: String = "XAF",
        location: Map<String, String>? = null,
        products: ArrayList<Map<String, Any>>? = null,
        customer: Map<String, String>? = null,
        trxID: String? = null,
    ): TransactionResponse {
        val endpoint = "payment/airtime/"

        val body: MutableMap<String, Any> = mapOf(
            "amount" to amount,
            "service" to service,
            "receiver" to receiver,
            "country" to country,
            "currency" to currency,
            "merchant" to merchant,
        ).toMutableMap()
        if (trxID != null) {
            body["trxID"] = trxID
        }
        if (location != null) {
            body["location"] = location
        }
        if (customer != null) {
            body["customer"] = customer
        }
        if (products != null) {
            body["products"] = products
        }

        return TransactionResponse(
            JSONObject(this.executeRequest(
                "POST",
                endpoint,
                Date(),
                nonce ?: nonce(),
                body
            ))
        )
    }

    /**
     * Get the current status of your service on MeSomb
     *
     * @return Application
     *
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
    fun getStatus(): Application {
        val endpoint = "payment/status/"
        return Application(JSONObject(this.executeRequest("GET", endpoint, Date())))
    }

    /**
     * Get transactions stored in MeSomb based on the list
     *
     * @param ids Ids of transactions to fetch
     * @param source Source of the transaction (default: MESOMB)
     *
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
    fun getTransactions(ids: Array<String>, source: String? = "MESOMB"): List<Transaction> {
        val endpoint = "payment/transactions/?" + ids.joinToString(separator = "&") { "ids=${it}" } + "&source=$source"
        return JSONArray(this.executeRequest("GET", endpoint, Date())).map { Transaction(it as JSONObject) }
    }

    /**
     * Reprocess transaction at the operators level to confirm the status of a transaction
     * @param ids list of transaction ids
     * @param source Source of the transaction (default: MESOMB)
     *
     * @return List of the transactions processed
     *
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
    fun checkTransactions(ids: Array<String>, source: String? = "MESOMB"): List<Transaction> {
        val endpoint = "payment/transactions/check/?" + ids.joinToString(separator = "&") { "ids=${it}" } + "&source=$source"
        return JSONArray(this.executeRequest("GET", endpoint, Date())).map { Transaction(it as JSONObject) }
    }

    @Throws(
        ServerException::class,
        ServiceNotFoundException::class,
        PermissionDeniedException::class,
        IOException::class,
        NoSuchAlgorithmException::class,
        InvalidClientRequestException::class,
        InvalidKeyException::class,
        ParseException::class
    )
    fun refundTransaction(
        id: String,
        amount: Double?,
        currency: String?,
        conversion: Boolean?
    ): TransactionResponse {
        val endpoint = "payment/refund/"

        val body: MutableMap<String, Any> = HashMap()
        body["id"] = id
        if (amount != null) {
            body["amount"] = amount
        }
        if (currency != null) {
            body["currency"] = currency
            body["amount_currency"] = currency
        }
        if (conversion != null) {
            body["conversion"] = conversion
        }

        return TransactionResponse(
            JSONObject(this.executeRequest(
                "POST",
                endpoint,
                Date(),
                nonce(),
                body
            ))
        )
    }

    override fun getService(): String {
        return "payment"
    }
}