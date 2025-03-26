package com.hachther.mesomb.operations

import com.hachther.mesomb.MeSomb
import com.hachther.mesomb.Signature
import com.hachther.mesomb.exceptions.InvalidClientRequestException
import com.hachther.mesomb.exceptions.PermissionDeniedException
import com.hachther.mesomb.exceptions.ServerException
import com.hachther.mesomb.exceptions.ServiceNotFoundException
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.net.MalformedURLException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.text.ParseException
import java.util.Date
import java.util.TreeMap
import java.util.concurrent.TimeUnit

abstract class AOperation(
    private var target: String,
    private var accessKey: String,
    private var secretKey: String,
    private var language: String,
) {
    companion object {
        val MEDIA_TYPE_JSON = "application/json; charset=utf-8".toMediaType()
    }

    private fun buildUrl(endpoint: String): String {
        return "${MeSomb.apiBase}/api/${MeSomb.apiVersion}/$endpoint"
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
            try {
                val data = JSONObject(message)
                message = data.getString("detail")
                code = data.optString("code")
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
    protected fun executeRequest(
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
        val authorization: String = if (method != "GET") {
            val headers = TreeMap<String, String>()
            headers["content-type"] = MEDIA_TYPE_JSON.toString()
            this.getAuthorization(method, endpoint, date, nonce, headers, body)
        } else {
            this.getAuthorization(method, endpoint, date, nonce)
        }
        val client: OkHttpClient = OkHttpClient.Builder().readTimeout(30, TimeUnit.SECONDS).build()
        var builder: Request.Builder = Request.Builder()
            .url(url)
            .method(method, if (body != null) JSONObject(body).toString().toRequestBody(MEDIA_TYPE_JSON) else null)
            .addHeader("x-mesomb-date", java.lang.String.valueOf(date.time / 1000))
            .addHeader("x-mesomb-nonce", nonce)
            .addHeader("Authorization", authorization)
            .addHeader("Accept-Language", language)
            .addHeader("X-MeSomb-Source", "MeSombKotlin/" + MeSomb.version)

        if (getService() == "payment") {
            builder = builder.addHeader("X-MeSomb-Application", target)
        }
        if (getService() == "wallet") {
            builder = builder.addHeader("X-MeSomb-Provider", target)
        }
        if (getService() == "fundraising") {
            builder = builder.addHeader("X-MeSomb-Fund", target)
        }

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
     * Get the service name
     * @return the service name
     */
    abstract fun getService(): String
}