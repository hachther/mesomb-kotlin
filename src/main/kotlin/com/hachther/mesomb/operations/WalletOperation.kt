package com.hachther.mesomb.operations

import com.hachther.mesomb.exceptions.InvalidClientRequestException
import com.hachther.mesomb.exceptions.PermissionDeniedException
import com.hachther.mesomb.exceptions.ServerException
import com.hachther.mesomb.exceptions.ServiceNotFoundException
import com.hachther.mesomb.models.PaginatedWalletTransactions
import com.hachther.mesomb.models.PaginatedWallets
import com.hachther.mesomb.models.Transaction
import com.hachther.mesomb.models.Wallet
import com.hachther.mesomb.models.WalletTransaction
import com.hachther.mesomb.util.RandomGenerator.nonce
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.util.Date

class WalletOperation(providerKey: String, accessKey: String, secretKey: String, language: String = "en") : AOperation(providerKey, accessKey, secretKey, language) {
    override fun getService(): String {
        return "wallet";
    }

    /**
     * Create a wallet
     *
     *
     * @param lastName The last name of the wallet owner
     * @param firstName The first name of the wallet owner
     * @param email The email of the wallet owner
     * @param phoneNumber The phone number of the wallet owner
     * @param country The country of the wallet owner
     * @param gender The gender of the wallet owner
     * @param number The unique numeric wallet identifier, if not set we will generate one for you
     * @param nonce The unique string on each request
     *
     * @return Wallet
     *
     * @throws ServerException if an error occurred on the server
     * @throws ServiceNotFoundException if the service is not found
     * @throws PermissionDeniedException if the permission is denied
     * @throws IOException if an error occurred while reading the response
     * @throws NoSuchAlgorithmException if the algorithm is not found
     * @throws InvalidClientRequestException if the request is invalid
     * @throws InvalidKeyException if the key is invalid
     */
    @Throws(
        ServerException::class,
        ServiceNotFoundException::class,
        PermissionDeniedException::class,
        IOException::class,
        NoSuchAlgorithmException::class,
        InvalidClientRequestException::class,
        InvalidKeyException::class,
    )
    fun createWallet(
        lastName: String,
        firstName: String? = null,
        email: String? = null,
        phoneNumber: String,
        country: String = "CM",
        gender: String,
        number: String? = null,
        nonce: String? = null
    ): Wallet {
        val endpoint = "wallet/wallets/"

        val body: MutableMap<String, Any> = mapOf(
            "last_name" to lastName,
            "phone_number" to phoneNumber,
            "country" to country,
            "gender" to gender,
        ).toMutableMap()

        if (firstName != null) {
            body["first_name"] = firstName
        }
        if (email != null) {
            body["email"] = email
        }
        if (number != null) {
            body["number"] = number
        }

        return Wallet(
            JSONObject(this.executeRequest("POST", endpoint, Date(), nonce ?: nonce(), body))
        )
    }

    /**
     * Get a wallet
     *
     * @param id The wallet identifier
     *
     * @return Wallet
     *
     * @throws ServerException if an error occurred on the server
     * @throws ServiceNotFoundException if the service is not found
     * @throws PermissionDeniedException if the permission is denied
     * @throws IOException if an error occurred while reading the response
     * @throws NoSuchAlgorithmException if the algorithm is not found
     * @throws InvalidClientRequestException if the request is invalid
     * @throws InvalidKeyException if the key is invalid
     */
    @Throws(
        ServerException::class,
        ServiceNotFoundException::class,
        PermissionDeniedException::class,
        IOException::class,
        NoSuchAlgorithmException::class,
        InvalidClientRequestException::class,
        InvalidKeyException::class,
    )
    fun getWallet(id: Long): Wallet {
        val endpoint = "wallet/wallets/$id/"

        return Wallet(
            JSONObject(this.executeRequest("GET", endpoint, Date(), nonce(), null))
        )
    }

    /**
     * Get wallets
     *
     * @param page The page number
     *
     * @return PaginatedWallets
     *
     * @throws ServerException if an error occurred on the server
     * @throws ServiceNotFoundException if the service is not found
     * @throws PermissionDeniedException if the permission is denied
     * @throws IOException if an error occurred while reading the response
     * @throws NoSuchAlgorithmException if the algorithm is not found
     * @throws InvalidClientRequestException if the request is invalid
     * @throws InvalidKeyException if the key is invalid
     */
    @Throws(
        ServerException::class,
        ServiceNotFoundException::class,
        PermissionDeniedException::class,
        IOException::class,
        NoSuchAlgorithmException::class,
        InvalidClientRequestException::class,
        InvalidKeyException::class,
    )
    fun getWallets(page: Int = 1): PaginatedWallets {
        val endpoint = "wallet/wallets/?page=$page"

        return PaginatedWallets(
            JSONObject(this.executeRequest("GET", endpoint, Date(), nonce(), null))
        )
    }

    /**
     * Update a wallet
     * @param id The wallet identifier
     *
     * @param lastName The last name of the wallet owner
     * @param firstName The first name of the wallet owner
     * @param email The email of the wallet owner
     * @param phoneNumber The phone number of the wallet owner
     * @param country The country of the wallet owner
     * @param gender The gender of the wallet owner
     * @param number The unique numeric wallet identifier, if not set we will generate one for you
     * @param nonce The unique string on each request
     *
     * @return Wallet
     *
     * @throws ServerException if an error occurred on the server
     * @throws ServiceNotFoundException if the service is not found
     * @throws PermissionDeniedException if the permission is denied
     * @throws IOException if an error occurred while reading the response
     * @throws NoSuchAlgorithmException if the algorithm is not found
     * @throws InvalidClientRequestException if the request is invalid
     * @throws InvalidKeyException if the key is invalid
     */
    @Throws(
        ServerException::class,
        ServiceNotFoundException::class,
        PermissionDeniedException::class,
        IOException::class,
        NoSuchAlgorithmException::class,
        InvalidClientRequestException::class,
        InvalidKeyException::class,
    )
    fun updateWallet(
        id: Long,
        lastName: String,
        firstName: String? = null,
        email: String? = null,
        phoneNumber: String,
        country: String = "CM",
        gender: String,
        number: String? = null,
        nonce: String? = null
    ): Wallet {
        val endpoint = "wallet/wallets/$id/"

        val body: MutableMap<String, Any> = mapOf(
            "last_name" to lastName,
            "phone_number" to phoneNumber,
            "country" to country,
            "gender" to gender,
        ).toMutableMap()

        if (firstName != null) {
            body["first_name"] = firstName
        }
        if (email != null) {
            body["email"] = email
        }
        if (number != null) {
            body["number"] = number
        }

        return Wallet(
            JSONObject(this.executeRequest("PUT", endpoint, Date(), nonce ?: nonce(), body))
        )
    }

    /**
     *
     * @param id The wallet identifier
     *
     * @throws ServerException if an error occurred on the server
     * @throws ServiceNotFoundException if the service is not found
     * @throws PermissionDeniedException if the permission is denied
     * @throws IOException if an error occurred while reading the response
     * @throws NoSuchAlgorithmException if the algorithm is not found
     * @throws InvalidClientRequestException if the request is invalid
     * @throws InvalidKeyException if the key is invalid
     */
    @Throws(
        ServerException::class,
        ServiceNotFoundException::class,
        PermissionDeniedException::class,
        IOException::class,
        NoSuchAlgorithmException::class,
        InvalidClientRequestException::class,
        InvalidKeyException::class,
    )
    fun deleteWallet(id: Long) {
        val endpoint = "wallet/wallets/$id/"

        this.executeRequest("DELETE", endpoint, Date(), nonce(), null)
    }

    /**
     * Remove money to a wallet
     *
     * @param wallet The wallet identifier
     * @param amount The amount to add
     * @param force Force the operation if balance is not enough
     * @param message The message to add to the transaction (optional)
     * @param externalId The external identifier of the transaction (optional)
     *
     * @return WalletTransaction
     *
     * @throws ServerException if an error occurred on the server
     * @throws ServiceNotFoundException if the service is not found
     * @throws PermissionDeniedException if the permission is denied
     * @throws IOException if an error occurred while reading the response
     * @throws NoSuchAlgorithmException if the algorithm is not found
     * @throws InvalidClientRequestException if the request is invalid
     * @throws InvalidKeyException if the key is invalid
     */
    @Throws(
        ServerException::class,
        ServiceNotFoundException::class,
        PermissionDeniedException::class,
        IOException::class,
        NoSuchAlgorithmException::class,
        InvalidClientRequestException::class,
        InvalidKeyException::class,
        java.text.ParseException::class
    )
    fun removeMoney(wallet: Long, amount: Double, force: Boolean = false, message: String? = null, externalId: String? = null): WalletTransaction {
        val endpoint = "wallet/wallets/$wallet/adjust/"

        val body: MutableMap<String, Any> = mutableMapOf(
            "amount" to amount,
            "force" to force,
            "direction" to -1
        )

        if (message != null) {
            body["message"] = message
        }

        if (externalId != null) {
            body["external_id"] = externalId
        }

        return WalletTransaction(
            JSONObject(this.executeRequest("POST", endpoint, Date(), nonce(), body))
        )
    }

    /**
     * Add money to a wallet
     *
     * @param wallet The wallet identifier
     * @param amount The amount to add
     * @param message The message to add to the transaction (optional)
     * @param externalId The external identifier of the transaction (optional)
     *
     * @return WalletTransaction
     *
     * @throws ServerException if an error occurred on the server
     * @throws ServiceNotFoundException if the service is not found
     * @throws PermissionDeniedException if the permission is denied
     * @throws IOException if an error occurred while reading the response
     * @throws NoSuchAlgorithmException if the algorithm is not found
     * @throws InvalidClientRequestException if the request is invalid
     * @throws InvalidKeyException if the key is invalid
     */
    @Throws(
        ServerException::class,
        ServiceNotFoundException::class,
        PermissionDeniedException::class,
        IOException::class,
        NoSuchAlgorithmException::class,
        InvalidClientRequestException::class,
        InvalidKeyException::class,
        java.text.ParseException::class
    )
    fun addMoney(wallet: Long, amount: Double, message: String? = null, externalId: String? = null): WalletTransaction {
        val endpoint = "wallet/wallets/$wallet/adjust/"

        val body: MutableMap<String, Any> = mutableMapOf(
            "amount" to amount,
            "direction" to 1
        )

        if (message != null) {
            body["message"] = message
        }

        if (externalId != null) {
            body["external_id"] = externalId
        }

        return WalletTransaction(
            JSONObject(this.executeRequest("POST", endpoint, Date(), nonce(), body))
        )
    }

    /**
     * Remove money to a wallet
     *
     * @param from The wallet identifier
     * @param to The wallet identifier
     * @param amount The amount to add
     * @param message The message to add to the transaction (optional)
     * @param externalId The external identifier of the transaction (optional)
     *
     * @return WalletTransaction
     *
     * @throws ServerException if an error occurred on the server
     * @throws ServiceNotFoundException if the service is not found
     * @throws PermissionDeniedException if the permission is denied
     * @throws IOException if an error occurred while reading the response
     * @throws NoSuchAlgorithmException if the algorithm is not found
     * @throws InvalidClientRequestException if the request is invalid
     * @throws InvalidKeyException if the key is invalid
     */
    @Throws(
        ServerException::class,
        ServiceNotFoundException::class,
        PermissionDeniedException::class,
        IOException::class,
        NoSuchAlgorithmException::class,
        InvalidClientRequestException::class,
        InvalidKeyException::class,
        java.text.ParseException::class
    )
    fun transferMoney(from: Long, to: Double, amount: Double, force: Boolean = false, message: String?, externalId: String?): WalletTransaction {
        val endpoint = "wallet/wallets/$from/transfer/"

        val body: MutableMap<String, Any> = mutableMapOf(
            "amount" to amount,
            "to" to to,
            "force" to force
        )

        if (message != null) {
            body["message"] = message
        }

        if (externalId != null) {
            body["external_id"] = externalId
        }

        return WalletTransaction(
            JSONObject(this.executeRequest("POST", endpoint, Date(), nonce(), body))
        )
    }

    /**
     * Get transactions
     *
     * @param page The page number
     * @param wallet The wallet identifier
     *
     * @return PaginatedWallets
     *
     * @throws ServerException if an error occurred on the server
     * @throws ServiceNotFoundException if the service is not found
     * @throws PermissionDeniedException if the permission is denied
     * @throws IOException if an error occurred while reading the response
     * @throws NoSuchAlgorithmException if the algorithm is not found
     * @throws InvalidClientRequestException if the request is invalid
     * @throws InvalidKeyException if the key is invalid
     */
    @Throws(
        ServerException::class,
        ServiceNotFoundException::class,
        PermissionDeniedException::class,
        IOException::class,
        NoSuchAlgorithmException::class,
        InvalidClientRequestException::class,
        InvalidKeyException::class,
    )
    fun listTransactions(page: Int = 1, wallet: Long? = null): PaginatedWalletTransactions {
        var endpoint = "wallet/transactions/?page=$page"
        if (wallet != null) {
            endpoint += "&wallet=$wallet"
        }

        return PaginatedWalletTransactions(
            JSONObject(this.executeRequest("GET", endpoint, Date(), nonce(), null))
        )
    }

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
    fun getTransactions(ids: Array<String>, source: String? = "MESOMB"): List<WalletTransaction> {
        val endpoint = "wallet/transactions/search/?" + ids.joinToString(separator = "&") { "ids=${it}" } + "&source=$source"

        return JSONArray(this.executeRequest("GET", endpoint, Date())).map { WalletTransaction(it as JSONObject) }
    }

    /**
     * Get transactions
     *
     * @param id The transaction identifier
     *
     * @return WalletTransaction
     *
     * @throws ServerException if an error occurred on the server
     * @throws ServiceNotFoundException if the service is not found
     * @throws PermissionDeniedException if the permission is denied
     * @throws IOException if an error occurred while reading the response
     * @throws NoSuchAlgorithmException if the algorithm is not found
     * @throws InvalidClientRequestException if the request is invalid
     * @throws InvalidKeyException if the key is invalid
     */
    @Throws(
        ServerException::class,
        ServiceNotFoundException::class,
        PermissionDeniedException::class,
        IOException::class,
        NoSuchAlgorithmException::class,
        InvalidClientRequestException::class,
        InvalidKeyException::class,
        java.text.ParseException::class
    )
    fun getTransaction(id: Long): WalletTransaction {
        val endpoint = "wallet/transactions/$id/"

        return WalletTransaction(
            JSONObject(this.executeRequest("GET", endpoint, Date(), nonce(), null))
        )
    }
}