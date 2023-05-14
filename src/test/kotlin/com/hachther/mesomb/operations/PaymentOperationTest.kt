package com.hachther.mesomb.operations

import com.hachther.mesomb.MeSomb
import com.hachther.mesomb.exceptions.InvalidClientRequestException
import com.hachther.mesomb.exceptions.PermissionDeniedException
import com.hachther.mesomb.exceptions.ServerException
import com.hachther.mesomb.exceptions.ServiceNotFoundException
import com.hachther.mesomb.models.Application
import com.hachther.mesomb.util.RandomGenerator.nonce
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.IOException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.util.*


class PaymentOperationTest {
    private val applicationKey = "2bb525516ff374bb52545bf22ae4da7d655ba9fd"
    private val accessKey = "c6c40b76-8119-4e93-81bf-bfb55417b392"
    private val secretKey = "fe8c2445-810f-4caa-95c9-778d51580163"

    @BeforeEach
    fun onSetup() {
        MeSomb.apiBase = "http://127.0.0.1:8000"
    }

    @Test
    fun testMakeCollectWithServiceNotFound() {
        val payment = PaymentOperation(applicationKey + "f", accessKey, secretKey)
        val exception: Exception = assertThrows(ServiceNotFoundException::class.java) {
            payment.makeCollect(
                5f,
                "MTN",
                "677550203",
                Date(),
                "fihser"
            )
        }
        Assertions.assertEquals("Application not found", exception.message)
    }

    @Test
    fun testMakeCollectWithPermissionDenied() {
        val payment = PaymentOperation(applicationKey, accessKey + "f", secretKey)
        val exception: java.lang.Exception = assertThrows(PermissionDeniedException::class.java) {
            payment.makeCollect(
                5f,
                "MTN",
                "677550203",
                Date(),
                "fihser"
            )
        }
        Assertions.assertEquals("Invalid access key", exception.message)
    }

    @Test
    fun testMakeCollectWithInvalidAmount() {
        val payment = PaymentOperation(applicationKey, accessKey, secretKey)
        val exception: java.lang.Exception = assertThrows(InvalidClientRequestException::class.java) {
            payment.makeCollect(
                5f,
                "MTN",
                "677550203",
                Date(),
                "fihser"
            )
        }
        Assertions.assertEquals("The amount should be greater than 10 XAF", exception.message)
    }

    @Test
    fun testMakeCollectWithSuccess() {
        val payment = PaymentOperation(applicationKey, accessKey, secretKey)
        try {
            val response = payment.makeCollect(100f, "MTN", "677550203", Date(), nonce())
            Assertions.assertTrue(response!!.isOperationSuccess())
            Assertions.assertTrue(response.isTransactionSuccess())
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
            val response =
                payment.makeCollect(100f, "MTN", "677550203", Date(), nonce(), "CM", "XAF", true, "asynchronous")
            Assertions.assertTrue(response!!.isOperationSuccess())
            Assertions.assertFalse(response.isTransactionSuccess())
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
        val exception: java.lang.Exception = assertThrows(ServiceNotFoundException::class.java) {
            payment.makeDeposit(
                5f,
                "MTN",
                "677550203",
                Date(),
                "fihser"
            )
        }
        Assertions.assertEquals("Application not found", exception.message)
    }

    @Test
    fun testMakeDepositWithPermissionDenied() {
        val payment = PaymentOperation(applicationKey, accessKey + "f", secretKey)
        val exception: java.lang.Exception = assertThrows(PermissionDeniedException::class.java) {
            payment.makeDeposit(
                5f,
                "MTN",
                "677550203",
                Date(),
                "fihser"
            )
        }
        Assertions.assertEquals("Invalid access key", exception.message)
    }

    @Test
    fun testMakeDepositWithInvalidAmount() {
        val payment = PaymentOperation(applicationKey, accessKey, secretKey)
        val exception: java.lang.Exception = assertThrows(InvalidClientRequestException::class.java) {
            payment.makeDeposit(
                5f,
                "MTN",
                "677550203",
                Date(),
                "fihser"
            )
        }
        Assertions.assertEquals("The amount should be greater than 10 XAF", exception.message)
    }

    @Test
    fun testMakeDepositWithSuccess() {
        val payment = PaymentOperation(applicationKey, accessKey, secretKey)
        try {
            val response = payment.makeDeposit(100f, "MTN", "677550203", Date(), nonce())
            Assertions.assertTrue(response!!.isOperationSuccess())
            Assertions.assertTrue(response.isTransactionSuccess())
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
    fun testUnSetWhitelistIPs() {
        val payment = PaymentOperation(applicationKey, accessKey, secretKey)
        try {
            val response: Application = payment.updateSecurity("whitelist_ips", "UNSET")
            Assertions.assertNull(response.getSecurityField("whitelist_ips"))
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
    fun testUnSetBlacklistReceivers() {
        val payment = PaymentOperation(applicationKey, accessKey, secretKey)
        try {
            val response: Application = payment.updateSecurity("blacklist_receivers", "UNSET")
            Assertions.assertNull(response.getSecurityField("blacklist_receivers"))
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
        val exception: java.lang.Exception = assertThrows(ServiceNotFoundException::class.java) {
            payment.getStatus()
        }
        Assertions.assertEquals("Application not found", exception.message)
    }

    @Test
    fun testGetStatusPermissionDenied() {
        val payment = PaymentOperation(applicationKey, accessKey + "f", secretKey)
        val exception: java.lang.Exception = assertThrows(PermissionDeniedException::class.java) {
            payment.getStatus()
        }
        Assertions.assertEquals("Invalid access key", exception.message)
    }

    @Test
    fun testGetStatusSuccess() {
        val payment = PaymentOperation(applicationKey, accessKey, secretKey)
        try {
            val application: Application = payment.getStatus()!!
            Assertions.assertEquals("Meudocta Shop", application.getName())
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
        val exception: java.lang.Exception = assertThrows(ServiceNotFoundException::class.java) {
            payment.getTransactions(
                arrayOf("c6c40b76-8119-4e93-81bf-bfb55417b392")
            )
        }
        Assertions.assertEquals("Application not found", exception.message)
    }

    @Test
    fun testGetTransactionsPermissionDenied() {
        val payment = PaymentOperation(applicationKey, accessKey + "f", secretKey)
        val exception: java.lang.Exception = assertThrows(PermissionDeniedException::class.java) {
            payment.getTransactions(
                arrayOf("c6c40b76-8119-4e93-81bf-bfb55417b392")
            )
        }
        Assertions.assertEquals("Invalid access key", exception.message)
    }

    @Test
    fun testGetTransactionsSuccess() {
        val payment = PaymentOperation(applicationKey, accessKey, secretKey)
        try {
            val transactions = payment.getTransactions(arrayOf("9886f099-dee2-4eaa-9039-e92b2ee33353"))
            Assertions.assertEquals(1, transactions!!.size)
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
        val exception: java.lang.Exception = assertThrows(ServiceNotFoundException::class.java) {
            payment.checkTransactions(
                arrayOf("c6c40b76-8119-4e93-81bf-bfb55417b392")
            )
        }
        Assertions.assertEquals("Application not found", exception.message)
    }

    @Test
    fun testCheckTransactionsPermissionDenied() {
        val payment = PaymentOperation(applicationKey, accessKey + "f", secretKey)
        val exception: java.lang.Exception = assertThrows(PermissionDeniedException::class.java) {
            payment.checkTransactions(
                arrayOf("c6c40b76-8119-4e93-81bf-bfb55417b392")
            )
        }
        Assertions.assertEquals("Invalid access key", exception.message)
    }

    @Test
    fun testCheckTransactionsSuccess() {
        val payment = PaymentOperation(applicationKey, accessKey, secretKey)
        try {
            val transactions = payment.checkTransactions(arrayOf("9886f099-dee2-4eaa-9039-e92b2ee33353"))
            Assertions.assertEquals(1, transactions!!.size)
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