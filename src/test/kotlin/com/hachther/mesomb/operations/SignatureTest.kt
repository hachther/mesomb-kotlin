package com.hachther.mesomb.operations;

import com.hachther.mesomb.Signature
import com.hachther.mesomb.util.RandomGenerator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.collections.ArrayList

class SignatureTest {
    val credentials: MutableMap<String, String?> = mutableMapOf(
        "accessKey" to "c6c40b76-8119-4e93-81bf-bfb55417b392",
        "secretKey" to "fe8c2445-810f-4caa-95c9-778d51580163",
    )
    @Test
    fun testSignatureWithGet() {
        val url: String = "http://127.0.0.1:8000/en/api/v1.1/payment/collect/"
        Assertions.assertEquals(Signature.signRequest("payment", "GET", url, Date(1673827200000), "fihser", credentials), "HMAC-SHA1 Credential=c6c40b76-8119-4e93-81bf-bfb55417b392/20230116/payment/mesomb_request, SignedHeaders=host;x-mesomb-date;x-mesomb-nonce, Signature=92866ff78427c739c1d48c9223a6133cde46ab5d")
    }
    @Test
    fun testSignatureWithPost() {
        val url: String = "http://127.0.0.1:8000/en/api/v1.1/payment/collect/"
        val products: MutableList<Map<String, Any>> = ArrayList()
        products.add(mapOf(
            "id" to "SKU001",
            "name" to "Sac a Main",
            "category" to "Sac"
        ))
        val body = mapOf(
            "amount" to 100f,
            "service" to "MTN",
            "payer" to "670000000",
            "trxID" to "1",
            "products" to products,
            "customer" to mapOf(
                "phone" to "+237677550439",
                "email" to "fisher.bank@gmail.com",
                "first_name" to "Fisher",
                "last_name" to "BANK"
            ),
            "location" to mapOf(
                "town" to "Douala",
                "country" to "Cameroun"
            ),
        )
        val headers = TreeMap<String, String>()
        headers["content-type"] = "application/json; charset=utf-8"
        Assertions.assertEquals(Signature.signRequest("payment", "POST", url, Date(1673827200000), "fihser", credentials, headers, body), "HMAC-SHA1 Credential=c6c40b76-8119-4e93-81bf-bfb55417b392/20230116/payment/mesomb_request, SignedHeaders=content-type;host;x-mesomb-date;x-mesomb-nonce, Signature=b5cd63f10e352b3184dc39f8665dba684efd170e")
    }
}
