package com.hachther.mesomb.operations

import com.hachther.mesomb.exceptions.InvalidClientRequestException
import com.hachther.mesomb.exceptions.PermissionDeniedException
import com.hachther.mesomb.exceptions.ServerException
import com.hachther.mesomb.exceptions.ServiceNotFoundException
import com.hachther.mesomb.models.Contribution
import com.hachther.mesomb.models.ContributionResponse
import com.hachther.mesomb.util.RandomGenerator.nonce
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.util.Date

class FundraisingOperation(
    fundKey: String,
    accessKey: String,
    secretKey: String,
    language: String = "en"
) : AOperation(
    fundKey, accessKey, secretKey, language
) {

    /**
     * Method to make a payment
     *
     * @param amount amount to collect
     * @param service payment service with the possible values MTN, ORANGE, AIRTEL
     * @param payer Account number to collect from
     * @param nonce unique string on each request
     * @param country 2 letters country code of the service (configured during your service registration in MeSomb)
     * @param currency currency of your service depending on your country
     * @param conversion true in case of foreign currently defined if you want to rely on MeSomb to convert the amount in the local currency
     * @param mode synchronous or asynchronous
     * @param location Map containing the location of the customer with the following attributes: town, region and location all string.
     * @param fullName a Map containing information about the customer: first_name string, last_name string
     * @param contact a Map containing information about the customer: phone_number string, email: string
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
        InvalidClientRequestException::class,
        java.text.ParseException::class
    )
    fun makeContribution(
        amount: Double,
        service: String,
        payer: String,
        nonce: String? = null,
        country: String = "CM",
        currency: String = "XAF",
        conversion: Boolean = false,
        mode: String = "synchronous",
        anonymous: Boolean = false,
        acceptTerms: Boolean = true,
        fullName: Map<String, String>? = null,
        contact: Map<String, String>? = null,
        location: Map<String, String>? = null,
        trxID: String? = null,
    ): ContributionResponse {
        val endpoint = "fundraising/contribute/"

        val body: MutableMap<String, Any> = mapOf(
            "amount" to amount,
            "service" to service,
            "payer" to payer,
            "country" to country,
            "currency" to currency,
            "conversion" to conversion,
            "anonymous" to anonymous,
            "accept_terms" to acceptTerms,
        ).toMutableMap()

        if (location != null) {
            body["location"] = location
        }

        if (trxID != null) {
            body["trxID"] = trxID
        }

        if (fullName != null) {
            body["full_name"] = fullName
        }

        if (contact != null) {
            body["contact"] = contact
        }

        return ContributionResponse(
            JSONObject(this.executeRequest(
                "POST",
                endpoint,
                Date(),
                nonce ?: nonce(),
                body,
                mode,
            ))
        )
    }


    /**
     * Get contributions stored in MeSomb based on the list
     *
     * @param ids Ids of contributions to fetch
     * @param source Source of the contribution with possible values MESOMB, EXTERNAL
     *
     * @return Contribution[] of the contributions fetched
     *
     * @throws IOException if an I/O error occurs
     * @throws NoSuchAlgorithmException if the algorithm is not supported
     * @throws InvalidKeyException if the key is invalid
     * @throws ServerException if the server encounters an error
     * @throws ServiceNotFoundException if the service is not found
     * @throws PermissionDeniedException if permission is denied
     * @throws InvalidClientRequestException if the request is invalid
     * @throws ParseException if the response cannot be parsed
     */
    @Throws(
        IOException::class,
        NoSuchAlgorithmException::class,
        InvalidKeyException::class,
        ServerException::class,
        ServiceNotFoundException::class,
        PermissionDeniedException::class,
        InvalidClientRequestException::class,
        java.text.ParseException::class
    )
    fun getContributions(ids: Array<String?>, source: String = "MESOMB"): List<Contribution> {
        val endpoint = "fundraising/contributions/?ids=" + java.lang.String.join(
            ",",
            *ids
        ) + "&source=" + source

        return (JSONArray(this.executeRequest("GET", endpoint, Date()))).map { Contribution(it as JSONObject) }
    }


    /**
     * Check contributions stored in MeSomb based on the list
     *
     * @param ids Ids of contributions to fetch
     *
     * @return Contribution[] of the contributions fetched
     *
     * @throws IOException if an I/O error occurs
     * @throws NoSuchAlgorithmException if the algorithm is not supported
     * @throws InvalidKeyException if the key is invalid
     * @throws ServerException if the server encounters an error
     * @throws ServiceNotFoundException if the service is not found
     * @throws PermissionDeniedException if permission is denied
     * @throws InvalidClientRequestException if the request is invalid
     * @throws ParseException if the response cannot be parsed
     */
    @Throws(
        IOException::class,
        NoSuchAlgorithmException::class,
        InvalidKeyException::class,
        ServerException::class,
        ServiceNotFoundException::class,
        PermissionDeniedException::class,
        InvalidClientRequestException::class,
        java.text.ParseException::class
    )
    fun checkContributions(ids: Array<String?>, source: String = "MESOMB"): List<Contribution> {
        val endpoint = "fundraising/contributions/check/?ids=" + java.lang.String.join(
            ",",
            *ids
        ) + "&source=" + source

        return (JSONArray(this.executeRequest("GET", endpoint, Date()))).map { Contribution(it as JSONObject) }
    }


    override fun getService(): String {
        return "fundraising"
    }
}
