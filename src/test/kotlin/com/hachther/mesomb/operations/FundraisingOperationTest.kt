package com.hachther.mesomb.operations

import com.hachther.mesomb.MeSomb
import com.hachther.mesomb.exceptions.InvalidClientRequestException
import com.hachther.mesomb.exceptions.PermissionDeniedException
import com.hachther.mesomb.exceptions.ServerException
import com.hachther.mesomb.exceptions.ServiceNotFoundException
import com.hachther.mesomb.models.Contribution
import org.junit.Before
import java.io.IOException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertNull

class FundraisingOperationTest {
    private val fundKey = "fa78bded201b791712ee398c7ddfb8652669404f"
    private val accessKey = "c6c40b76-8119-4e93-81bf-bfb55417b392"
    private val secretKey = "fe8c2445-810f-4caa-95c9-778d51580163"

    @Before
    fun onSetup() {
        MeSomb.apiBase = "http://127.0.0.1:8000"
    }

    @Test
    fun testMakeContributeWithServiceNotFound() {
        val payment = FundraisingOperation(this.fundKey + "f", this.accessKey, this.secretKey)
        val exception: Exception = assertFailsWith(
            ServiceNotFoundException::class
        ) {
            payment.makeContribution(amount = 5.0, service = "MTN", payer = "670000000")
        }
        assertEquals("Fund not found", exception.message)
    }

    @Test
    fun testMakeContributeWithPermissionDenied() {
        val payment = FundraisingOperation(this.fundKey, this.accessKey + "f", this.secretKey)
        val exception: Exception = assertFailsWith(
            PermissionDeniedException::class
        ) {
            payment.makeContribution(amount = 5.0, service = "MTN", payer = "670000000")
        }
        assertEquals("Invalid access key", exception.message)
    }

    @Test
    fun testMakeContributeWithSuccess() {
        val payment = FundraisingOperation(this.fundKey, this.accessKey, this.secretKey)
        try {
            val response = payment.makeContribution(
                amount = 100.0,
                service = "MTN",
                payer = "670000000",
                trxID = "1",
                fullName = mapOf("first_name" to "John", "last_name" to "Doe"),
                contact = mapOf("email" to "contact@gmail.com", "phone_number" to "+237677550203")
            )
            assertTrue(response.isOperationSuccess)
            assertTrue(response.isContributionSuccess)
            assertEquals(response.status, "SUCCESS")
            assertEquals(response.contribution.amount, 98.0)
            assertEquals(response.contribution.fees, 2.0)
            assertEquals(response.contribution.bParty, "237670000000")
            assertEquals(response.contribution.country, "CM")
            assertEquals(response.contribution.currency, "XAF")
            assertEquals(response.contribution.reference, "1")
            assertEquals(response.contribution.service, "MTN")
            assertEquals(response.contribution.contributor!!.email, "contact@gmail.com")
            assertEquals(response.contribution.contributor!!.phone, "+237677550203")
            assertEquals(response.contribution.contributor!!.firstName, "John")
            assertEquals(response.contribution.contributor!!.lastName, "Doe")
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
        } catch (e: java.text.ParseException) {
            throw RuntimeException(e)
        }
    }

    @Test
    fun testMakeContributeWithSuccessWithAnonymous() {
        val payment = FundraisingOperation(this.fundKey, this.accessKey, this.secretKey)
        try {
            val response = payment.makeContribution(
                amount = 100.0,
                service = "MTN",
                payer = "670000000",
                trxID = "1",
                anonymous = true
            )
            assertTrue(response.isOperationSuccess)
            assertTrue(response.isContributionSuccess)
            assertEquals(response.status, "SUCCESS")
            assertEquals(response.contribution.amount, 98.0)
            assertEquals(response.contribution.fees, 2.0)
            assertEquals(response.contribution.bParty, "237670000000")
            assertEquals(response.contribution.country, "CM")
            assertEquals(response.contribution.currency, "XAF")
            assertEquals(response.contribution.reference, "1")
            assertEquals(response.contribution.service, "MTN")
            assertNull(response.contribution.contributor)
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
        } catch (e: java.text.ParseException) {
            throw RuntimeException(e)
        }
    }

    @Test
    fun testGetTransactionsSuccess() {
        val payment = FundraisingOperation(this.fundKey, this.accessKey, this.secretKey)
        try {
            val transactions: List<Contribution> =
                payment.getContributions(arrayOf("0685831f-4145-4352-ae81-155fec42c748"))
            assertEquals(1, transactions.size)
            assertEquals("0685831f-4145-4352-ae81-155fec42c748", transactions[0].pk)
        } catch (e: ServerException) {
            throw RuntimeException(e)
        } catch (e: ServiceNotFoundException) {
            throw RuntimeException(e)
        } catch (e: PermissionDeniedException) {
            throw RuntimeException(e)
        } catch (e: IOException) {
            throw RuntimeException(e)
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException(e)
        } catch (e: InvalidClientRequestException) {
            throw RuntimeException(e)
        } catch (e: InvalidKeyException) {
            throw RuntimeException(e)
        } catch (e: java.text.ParseException) {
            throw RuntimeException(e)
        }
    }

    @Test
    fun testCheckTransactionsSuccess() {
        val payment = FundraisingOperation(this.fundKey, this.accessKey, this.secretKey)
        try {
            val transactions: List<Contribution> =
                payment.getContributions(arrayOf("0685831f-4145-4352-ae81-155fec42c748"))
            assertEquals(1, transactions.size)
            assertEquals("0685831f-4145-4352-ae81-155fec42c748", transactions[0].pk)
        } catch (e: ServerException) {
            throw RuntimeException(e)
        } catch (e: ServiceNotFoundException) {
            throw RuntimeException(e)
        } catch (e: PermissionDeniedException) {
            throw RuntimeException(e)
        } catch (e: IOException) {
            throw RuntimeException(e)
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException(e)
        } catch (e: InvalidClientRequestException) {
            throw RuntimeException(e)
        } catch (e: InvalidKeyException) {
            throw RuntimeException(e)
        } catch (e: java.text.ParseException) {
            throw RuntimeException(e)
        }
    }
}