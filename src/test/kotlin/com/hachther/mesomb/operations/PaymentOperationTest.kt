package com.hachther.mesomb.operations

import com.hachther.mesomb.MeSomb
import com.hachther.mesomb.exceptions.InvalidClientRequestException
import com.hachther.mesomb.exceptions.PermissionDeniedException
import com.hachther.mesomb.exceptions.ServerException
import com.hachther.mesomb.exceptions.ServiceNotFoundException
import com.hachther.mesomb.models.Application
import org.junit.Test
import org.junit.Before
import kotlin.test.assertNotNull
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFailsWith
import java.io.IOException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException


class PaymentOperationTest {
    private val applicationKey = "2bb525516ff374bb52545bf22ae4da7d655ba9fd"
    private val accessKey = "c6c40b76-8119-4e93-81bf-bfb55417b392"
    private val secretKey = "fe8c2445-810f-4caa-95c9-778d51580163"

    @Before
    fun onSetup() {
        MeSomb.apiBase = "http://127.0.0.1:8000"
    }

    @Test
    fun testMakeCollectWithServiceNotFound() {
        val payment = PaymentOperation(applicationKey + "f", accessKey, secretKey)
        val exception: Exception = assertFailsWith(ServiceNotFoundException::class) {
            payment.makeCollect(amount = 5.0, service = "MTN", payer = "670000000")
        }
        assertEquals("Application not found", exception.message)
    }

    @Test
    fun testMakeCollectWithPermissionDenied() {
        val payment = PaymentOperation(applicationKey, accessKey + "f", secretKey)
        val exception: java.lang.Exception = assertFailsWith(PermissionDeniedException::class) {
            payment.makeCollect(amount = 5.0, service = "MTN", payer = "670000000")
        }
        assertEquals("Invalid access key", exception.message)
    }

//    @Test
//    fun testMakeCollectWithInvalidAmount() {
//        val payment = PaymentOperation(applicationKey, accessKey, secretKey)
//        val exception: java.lang.Exception = assertFailsWith(InvalidClientRequestException::class) {
//            payment.makeCollect(amount = 5.0, service = "MTN", payer = "670000000")
//        }
//        assertEquals("The amount should be greater than 10 XAF", exception.message)
//    }

    @Test
    fun testMakeCollectWithSuccess() {
        val payment = PaymentOperation(applicationKey, accessKey, secretKey)
        try {
            val response = payment.makeCollect(amount = 100.0, service = "MTN", payer = "670000000", trxID = "1")
            assertTrue(response.isOperationSuccess())
            assertTrue(response.isTransactionSuccess())
            assertEquals(response.status, "SUCCESS")
            assertEquals(response.transaction.amount, 98.toDouble())
            assertEquals(response.transaction.fees, 2.toDouble())
            assertEquals(response.transaction.service, "MTN")
            assertEquals(response.transaction.bParty, "237670000000")
            assertEquals(response.transaction.country, "CM")
            assertEquals(response.transaction.currency, "XAF")
            assertEquals(response.transaction.reference, "1")
            assertNotNull(response.transaction.getData())
        } catch (e: IOException) {
            throw RuntimeException(e)
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException(e)
        } catch (e: InvalidKeyException) {
            throw RuntimeException(e)
        } catch (e: ServerException) {
            throw RuntimeException(e)
        } catch (e: ServiceNotFoundException) {
            throw RuntimeException(e)
        } catch (e: PermissionDeniedException) {
            throw RuntimeException(e)
        } catch (e: InvalidClientRequestException) {
            throw RuntimeException(e)
        }
    }

    @Test
    fun testMakeCollectWithSuccessAndProducts() {
        val payment = PaymentOperation(applicationKey, accessKey, secretKey)
        try {
            val response = payment.makeCollect(
                amount = 100.0,
                service = "MTN",
                payer = "670000000",
                trxID = "1",
                products = listOf(
                    mapOf(
                        "id" to "SKU001",
                        "name" to "Sac a Main",
                        "category" to "Sac"
                    )
                ),
                customer = mapOf(
                    "phone" to "+237677550439",
                    "email" to "fisher.bank@gmail.com",
                    "first_name" to "Fisher",
                    "last_name" to "BANK"
                ),
                location = mapOf(
                    "town" to "Douala",
                    "country" to "Cameroun"
                )
            )
            val products: MutableList<Map<String, Any>> = ArrayList()
            products.add(mapOf(
                "id" to "SKU001",
                "name" to "Sac a Main",
                "category" to "Sac"
            ))
            assertTrue(response.isOperationSuccess())
            assertTrue(response.isTransactionSuccess())
            assertEquals(response.status, "SUCCESS")
            assertEquals(response.transaction.amount, 98.toDouble())
            assertEquals(response.transaction.fees, 2.toDouble())
            assertEquals(response.transaction.bParty, "237670000000")
            assertEquals(response.transaction.country, "CM")
            assertEquals(response.transaction.currency, "XAF")
            assertEquals(response.transaction.reference, "1")
            assertEquals(response.transaction.customer?.phone, "+237677550439")
            assertEquals(response.transaction.customer?.email, "fisher.bank@gmail.com")
            assertEquals(response.transaction.customer?.firstName, "Fisher")
            assertEquals(response.transaction.customer?.lastName, "BANK")
            assertEquals(response.transaction.location?.town, "Douala")
            assertEquals(response.transaction.location?.country, "Cameroun")
            assertEquals(response.transaction.products.size, 1)
            assertEquals(response.transaction.products.first()?.id, "SKU001")
            assertEquals(response.transaction.products.first()?.name, "Sac a Main")
            assertEquals(response.transaction.products.first()?.category, "Sac")
        } catch (e: IOException) {
            throw RuntimeException(e)
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException(e)
        } catch (e: InvalidKeyException) {
            throw RuntimeException(e)
        } catch (e: ServerException) {
            throw RuntimeException(e)
        } catch (e: ServiceNotFoundException) {
            throw RuntimeException(e)
        } catch (e: PermissionDeniedException) {
            throw RuntimeException(e)
        } catch (e: InvalidClientRequestException) {
            throw RuntimeException(e)
        }
    }

    @Test
    fun testMakeCollectWithPending() {
        val payment = PaymentOperation(applicationKey, accessKey, secretKey)
        try {
            val response = payment.makeCollect(amount = 100.0, service = "MTN", payer = "670000000", mode = "asynchronous")
            assertTrue(response.isOperationSuccess())
            assertTrue(response.isTransactionSuccess())
//            assertEquals(response.transaction.status, "PENDING")
        } catch (e: IOException) {
            throw RuntimeException(e)
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException(e)
        } catch (e: InvalidKeyException) {
            throw RuntimeException(e)
        } catch (e: ServerException) {
            throw RuntimeException(e)
        } catch (e: ServiceNotFoundException) {
            throw RuntimeException(e)
        } catch (e: PermissionDeniedException) {
            throw RuntimeException(e)
        } catch (e: InvalidClientRequestException) {
            throw RuntimeException(e)
        }
    }

    @Test
    fun testMakeDepositWithServiceNotFound() {
        val payment = PaymentOperation(applicationKey + "f", accessKey, secretKey)
        val exception: java.lang.Exception = assertFailsWith(ServiceNotFoundException::class) {
            payment.makeDeposit(amount = 5.0, service = "MTN", receiver = "670000000")
        }
        assertEquals("Application not found", exception.message)
    }

    @Test
    fun testMakeDepositWithPermissionDenied() {
        val payment = PaymentOperation(applicationKey, accessKey + "f", secretKey)
        val exception: java.lang.Exception = assertFailsWith(PermissionDeniedException::class) {
            payment.makeDeposit(amount = 5.0, service = "MTN", receiver = "670000000")
        }
        assertEquals("Invalid access key", exception.message)
    }

//    @Test
//    fun testMakeDepositWithInvalidAmount() {
//        val payment = PaymentOperation(applicationKey, accessKey, secretKey)
//        val exception: java.lang.Exception = assertFailsWith(InvalidClientRequestException::class) {
//            payment.makeDeposit(amount = 5.0, service = "MTN", receiver = "670000000")
//        }
//        assertEquals("The amount should be greater than 10 XAF", exception.message)
//    }

    @Test
    fun testMakeDepositWithSuccess() {
        val payment = PaymentOperation(applicationKey, accessKey, secretKey)
        try {
            val response = payment.makeDeposit(amount = 100.0, service = "MTN", receiver = "670000000", trxID = "1")
            assertTrue(response.isOperationSuccess())
            assertTrue(response.isTransactionSuccess())
            assertEquals(response.status, "SUCCESS")
            assertEquals(response.transaction.amount, 100.0)
            assertEquals(response.transaction.fees, 1.01)
            assertEquals(response.transaction.bParty, "237670000000")
            assertEquals(response.transaction.country, "CM")
            assertEquals(response.transaction.currency, "XAF")
            assertEquals(response.transaction.reference, "1")
        } catch (e: IOException) {
            throw java.lang.RuntimeException(e)
        } catch (e: NoSuchAlgorithmException) {
            throw java.lang.RuntimeException(e)
        } catch (e: InvalidKeyException) {
            throw java.lang.RuntimeException(e)
        } catch (e: ServerException) {
            throw java.lang.RuntimeException(e)
        } catch (e: ServiceNotFoundException) {
            throw java.lang.RuntimeException(e)
        } catch (e: PermissionDeniedException) {
            throw java.lang.RuntimeException(e)
        } catch (e: InvalidClientRequestException) {
            throw java.lang.RuntimeException(e)
        }
    }

    @Test
    fun testGetStatusNotServiceFound() {
        val payment = PaymentOperation(applicationKey + "f", accessKey, secretKey)
        val exception: java.lang.Exception = assertFailsWith(ServiceNotFoundException::class) {
            payment.getStatus()
        }
        assertEquals("Application not found", exception.message)
    }

    @Test
    fun testGetStatusPermissionDenied() {
        val payment = PaymentOperation(applicationKey, accessKey + "f", secretKey)
        val exception: java.lang.Exception = assertFailsWith(PermissionDeniedException::class) {
            payment.getStatus()
        }
        assertEquals("Invalid access key", exception.message)
    }

    @Test
    fun testGetStatusSuccess() {
        val payment = PaymentOperation(applicationKey, accessKey, secretKey)
        try {
            val application: Application = payment.getStatus()
            assertEquals("Meudocta Shop", application.name)
        } catch (e: ServerException) {
            throw java.lang.RuntimeException(e)
        } catch (e: ServiceNotFoundException) {
            throw java.lang.RuntimeException(e)
        } catch (e: PermissionDeniedException) {
            throw java.lang.RuntimeException(e)
        } catch (e: IOException) {
            throw java.lang.RuntimeException(e)
        } catch (e: NoSuchAlgorithmException) {
            throw java.lang.RuntimeException(e)
        } catch (e: InvalidClientRequestException) {
            throw java.lang.RuntimeException(e)
        } catch (e: InvalidKeyException) {
            throw java.lang.RuntimeException(e)
        }
    }

    @Test
    fun testGetTransactionsNotServiceFound() {
        val payment = PaymentOperation(applicationKey + "f", accessKey, secretKey)
        val exception: java.lang.Exception = assertFailsWith(ServiceNotFoundException::class) {
            payment.getTransactions(
                arrayOf("c6c40b76-8119-4e93-81bf-bfb55417b392")
            )
        }
        assertEquals("Application not found", exception.message)
    }

    @Test
    fun testGetTransactionsPermissionDenied() {
        val payment = PaymentOperation(applicationKey, accessKey + "f", secretKey)
        val exception: java.lang.Exception = assertFailsWith(PermissionDeniedException::class) {
            payment.getTransactions(
                arrayOf("c6c40b76-8119-4e93-81bf-bfb55417b392")
            )
        }
        assertEquals("Invalid access key", exception.message)
    }

    @Test
    fun testGetTransactionsSuccess() {
        val payment = PaymentOperation(applicationKey, accessKey, secretKey)
        try {
            val transactions = payment.getTransactions(arrayOf("a483a9e8-51d7-44c9-875b-1305b1801274", "a483a9e8-51d7-44c9-875b-1305b1801273"))
            assertEquals(1, transactions.size)
            assertEquals(transactions.first().pk, "a483a9e8-51d7-44c9-875b-1305b1801274")
        } catch (e: ServerException) {
            throw java.lang.RuntimeException(e)
        } catch (e: ServiceNotFoundException) {
            throw java.lang.RuntimeException(e)
        } catch (e: PermissionDeniedException) {
            throw java.lang.RuntimeException(e)
        } catch (e: IOException) {
            throw java.lang.RuntimeException(e)
        } catch (e: NoSuchAlgorithmException) {
            throw java.lang.RuntimeException(e)
        } catch (e: InvalidClientRequestException) {
            throw java.lang.RuntimeException(e)
        } catch (e: InvalidKeyException) {
            throw java.lang.RuntimeException(e)
        }
    }

    @Test
    fun testCheckTransactionsNotServiceFound() {
        val payment = PaymentOperation(applicationKey + "f", accessKey, secretKey)
        val exception: java.lang.Exception = assertFailsWith(ServiceNotFoundException::class) {
            payment.checkTransactions(
                arrayOf("c6c40b76-8119-4e93-81bf-bfb55417b392")
            )
        }
        assertEquals("Application not found", exception.message)
    }

    @Test
    fun testCheckTransactionsPermissionDenied() {
        val payment = PaymentOperation(applicationKey, accessKey + "f", secretKey)
        val exception: java.lang.Exception = assertFailsWith(PermissionDeniedException::class) {
            payment.checkTransactions(
                arrayOf("c6c40b76-8119-4e93-81bf-bfb55417b392")
            )
        }
        assertEquals("Invalid access key", exception.message)
    }

    @Test
    fun testCheckTransactionsSuccess() {
        val payment = PaymentOperation(applicationKey, accessKey, secretKey)
        try {
            val transactions = payment.checkTransactions(arrayOf("a483a9e8-51d7-44c9-875b-1305b1801274", "a483a9e8-51d7-44c9-875b-1305b1801273"))
            assertEquals(1, transactions.size)
            assertEquals(transactions.first().pk, "a483a9e8-51d7-44c9-875b-1305b1801274")
        } catch (e: ServerException) {
            throw java.lang.RuntimeException(e)
        } catch (e: ServiceNotFoundException) {
            throw java.lang.RuntimeException(e)
        } catch (e: PermissionDeniedException) {
            throw java.lang.RuntimeException(e)
        } catch (e: IOException) {
            throw java.lang.RuntimeException(e)
        } catch (e: NoSuchAlgorithmException) {
            throw java.lang.RuntimeException(e)
        } catch (e: InvalidClientRequestException) {
            throw java.lang.RuntimeException(e)
        } catch (e: InvalidKeyException) {
            throw java.lang.RuntimeException(e)
        }
    }
}