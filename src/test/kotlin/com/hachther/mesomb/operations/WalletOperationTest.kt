package com.hachther.mesomb.operations

import com.hachther.mesomb.MeSomb
import com.hachther.mesomb.exceptions.InvalidClientRequestException
import com.hachther.mesomb.exceptions.PermissionDeniedException
import com.hachther.mesomb.exceptions.ServerException
import com.hachther.mesomb.exceptions.ServiceNotFoundException
import com.hachther.mesomb.models.PaginatedWalletTransactions
import com.hachther.mesomb.models.WalletTransaction
import org.junit.Before
import java.io.IOException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlin.test.assertNull

class WalletOperationTest {
    private val providerKey = "a1dc7a7391c538788043"
    private val accessKey = "c6c40b76-8119-4e93-81bf-bfb55417b392"
    private val secretKey = "fe8c2445-810f-4caa-95c9-778d51580163"

    @Before
    fun onSetup() {
        MeSomb.apiBase = "http://127.0.0.1:8000"
    }

    @Test
    @Throws(
        ServerException::class,
        ServiceNotFoundException::class,
        PermissionDeniedException::class,
        IOException::class,
        NoSuchAlgorithmException::class,
        InvalidClientRequestException::class,
        InvalidKeyException::class
    )
    fun testCreateWalletWithSuccess() {
        val client = WalletOperation(this.providerKey, this.accessKey, this.secretKey)
        val wallet = client.createWallet(
            lastName = "Doe",
            phoneNumber="+237677550000",
            gender = "MAN",
            firstName = "John",
            email = "contact@gmail.com",
            country = "CM"
        )

        assertEquals(wallet.firstName, "John")
        assertEquals(wallet.lastName, "Doe")
        assertEquals(wallet.email, "contact@gmail.com")
        assertEquals(wallet.phoneNumber, "+237677550000")
        assertEquals(wallet.country, "CM")
        assertEquals(wallet.gender, "MAN")
        assertNotNull(wallet.number)
    }

    @Test
    @Throws(
        ServerException::class,
        ServiceNotFoundException::class,
        PermissionDeniedException::class,
        IOException::class,
        NoSuchAlgorithmException::class,
        InvalidClientRequestException::class,
        InvalidKeyException::class
    )
    fun testCreateWalletWithMinValueSuccess() {
        val client = WalletOperation(this.providerKey, this.accessKey, this.secretKey)
        val wallet = client.createWallet(lastName = "Doe", phoneNumber="+237677550000", gender = "MAN")

        assertEquals(wallet.lastName, "Doe")
        assertEquals(wallet.phoneNumber, "+237677550000")
        assertEquals(wallet.gender, "MAN")
        assertNotNull(wallet.number)
    }

    @Test
    @Throws(
        ServerException::class,
        ServiceNotFoundException::class,
        PermissionDeniedException::class,
        IOException::class,
        NoSuchAlgorithmException::class,
        InvalidClientRequestException::class,
        InvalidKeyException::class
    )
    fun testUpdateWalletWithMinValueSuccess() {
        val client = WalletOperation(this.providerKey, this.accessKey, this.secretKey)
        val wallet = client.updateWallet(229L, lastName = "Doe", phoneNumber="+237677550005", gender = "WOMAN")

        assertEquals(wallet.id, 229)
        assertEquals(wallet.lastName, "Doe")
        assertEquals(wallet.phoneNumber, "+237677550005")
        assertEquals(wallet.gender, "WOMAN")
        assertNotNull(wallet.number)
    }

    @Test
    @Throws(
        ServerException::class,
        ServiceNotFoundException::class,
        PermissionDeniedException::class,
        IOException::class,
        NoSuchAlgorithmException::class,
        InvalidClientRequestException::class,
        InvalidKeyException::class
    )
    fun testGetWalletWithSuccess() {
        val client = WalletOperation(this.providerKey, this.accessKey, this.secretKey)
        val wallet = client.getWallet(228L)

        assertEquals(wallet.id, 228)
        assertEquals(wallet.firstName, "John")
        assertEquals(wallet.lastName, "Doe")
        assertEquals(wallet.email, "contact@gmail.com")
        assertEquals(wallet.phoneNumber, "+237677550000")
        assertEquals(wallet.country, "CM")
        assertEquals(wallet.gender, "MAN")
        assertNotNull(wallet.number)
    }

    @Test
    @Throws(
        ServerException::class,
        ServiceNotFoundException::class,
        PermissionDeniedException::class,
        IOException::class,
        NoSuchAlgorithmException::class,
        InvalidClientRequestException::class,
        InvalidKeyException::class
    )
    fun testGetWalletsWithSuccess() {
        val client = WalletOperation(this.providerKey, this.accessKey, this.secretKey)
        val pagination = client.getWallets(1)
        assertTrue(pagination.count > 0)
        assertTrue(pagination.results!!.size > 0)
        assertNull(pagination.previous)
    }

    @Test
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
    fun testAddMoneyToWalletWithSuccess() {
        val client = WalletOperation(this.providerKey, this.accessKey, this.secretKey)
        val wallet = client.getWallet(228L)
        val transaction = client.addMoney(228L, 10000.0)

        assertEquals(transaction.direction, 1)
        assertEquals(transaction.status, "SUCCESS")
        assertEquals(transaction.amount, 10000.0)
        assertEquals(
            transaction.balanceAfter,
            (if (wallet.balance != null) wallet.balance else 0.0)!! + 10000
        )
        assertEquals(transaction.wallet, 228)
        assertEquals(transaction.country, "CM")
        assertNotNull(transaction.finTrxId)
        assertNotNull(transaction.date)
    }

    @Test
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
    fun testRemoveMoneyToWalletWithSuccess() {
        val client = WalletOperation(this.providerKey, this.accessKey, this.secretKey)
        val wallet = client.getWallet(228L)
        val transaction = client.removeMoney(228L, 10000.0)

        assertEquals(transaction.direction, -1)
        assertEquals(transaction.status, "SUCCESS")
        assertEquals(transaction.amount, 10000.0)
        assertEquals(
            transaction.balanceAfter,
            (if (wallet.balance != null) wallet.balance else 0.0)!! - 10000
        )
        assertEquals(transaction.wallet, 228)
        assertEquals(transaction.country, "CM")
        assertNotNull(transaction.finTrxId)
        assertNotNull(transaction.date)
    }

    @Test
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
    fun testGetTransactionDetailWithSuccess() {
        val client = WalletOperation(this.providerKey, this.accessKey, this.secretKey)
        val transaction = client.getTransaction(3061L)

        assertEquals(transaction.id, 3061L)
        assertEquals(transaction.direction, -1)
        assertEquals(transaction.status, "SUCCESS")
        assertEquals(transaction.amount, 1000.0)
        assertEquals(transaction.balanceAfter, 1000.0)
        assertEquals(transaction.wallet, 228)
        assertEquals(transaction.country, "CM")
        assertNotNull(transaction.finTrxId)
        assertNotNull(transaction.date)
    }


    @Test
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
    fun testListTransactionsWithSuccess() {
        val client = WalletOperation(this.providerKey, this.accessKey, this.secretKey)
        val transaction: PaginatedWalletTransactions = client.listTransactions(1)

        assertNull(transaction.previous)
        assertTrue(transaction.count > 0)
        transaction.results?.let { assertTrue(it.isNotEmpty()) }
    }

    @Test
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
    fun testGetTransactionsWithSuccess() {
        val client = WalletOperation(this.providerKey, this.accessKey, this.secretKey)
        val transactions: List<WalletTransaction> =
            client.getTransactions(arrayOf("620757", "620756"))

        assertTrue(transactions.isNotEmpty())
    }
}