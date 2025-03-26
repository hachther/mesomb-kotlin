package com.hachther.mesomb

import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.net.MalformedURLException
import java.net.URL
import java.net.URLEncoder
import java.security.InvalidKeyException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.SimpleDateFormat
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec


object Signature {
    fun bytesToHex(bytes: ByteArray): String {
        val hexArray = "0123456789abcdef".toCharArray()
        val hexChars = CharArray(bytes.size * 2)
        var j = 0
        var v: Int
        while (j < bytes.size) {
            v = bytes[j].toInt() and 0xFF
            hexChars[j * 2] = hexArray[v ushr 4]
            hexChars[j * 2 + 1] = hexArray[v and 0x0F]
            j++
        }
        return String(hexChars)
    }

    @Throws(NoSuchAlgorithmException::class)
    fun sha1(input: String): String {
        val md = MessageDigest.getInstance("SHA-1")
        return bytesToHex(md.digest(input.toByteArray()))
    }

    @Throws(NoSuchAlgorithmException::class, InvalidKeyException::class)
    fun hmacSha1(key: String?, input: String): String {
        val mac = Mac.getInstance("HmacSHA1")
        mac.init(SecretKeySpec(key!!.toByteArray(), "HmacSHA1"))
        return bytesToHex(mac.doFinal(input.toByteArray()))
    }

    @Throws(MalformedURLException::class, NoSuchAlgorithmException::class, InvalidKeyException::class)
    fun signRequest(
        service: String,
        method: String,
        url: String?,
        date: Date,
        nonce: String,
        credentials: MutableMap<String, String?>,
        defaultHeaders: TreeMap<String, String>? = null,
        body: Map<String, Any?>? = null
    ): String {
        var headers = defaultHeaders
        val algorithm = MeSomb.algorithm
        val parse = URL(url)
        val canonicalQuery = if (parse.getQuery() != null) parse.getQuery() else ""
        val timestamp: Long = date.getTime() / 1000
        if (headers == null) {
            headers = TreeMap()
        }
        headers["host"] =
            (parse.getProtocol() + "://" + parse.getHost()) + if (parse.getPort() > 0) ":" + parse.getPort() else ""
        headers["x-mesomb-date"] = timestamp.toString()
        headers["x-mesomb-nonce"] = nonce

        // String[] headersKeys = (String[]) headers.keySet().toArray();
        val headersTokens = arrayOfNulls<String>(headers.size)
        val headersKeys = arrayOfNulls<String>(headers.size)
        var i = 0
        for (key in headers.keys) {
            headersTokens[i] = key + ":" + headers[key]
            headersKeys[i] = key
            i++
        }
        val canonicalHeaders = java.lang.String.join("\n", *headersTokens)
        val payloadHash = sha1(if (body != null) JSONObject(body).toString().replace("\\/", "/") else "{}")
        val signedHeaders = java.lang.String.join(";", *headersKeys)
        val path: String
        path = try {
            URLEncoder.encode(parse.getPath(), "UTF-8").replace("%2F".toRegex(), "/")
        } catch (e: UnsupportedEncodingException) {
            throw RuntimeException(e)
        }
        val canonicalRequest = "$method\n$path\n$canonicalQuery\n$canonicalHeaders\n$signedHeaders\n$payloadHash"
        val dateFormat = SimpleDateFormat("yyyyMMdd")
        val scope = dateFormat.format(date) + "/" + service + "/mesomb_request"
        val stringToSign = "$algorithm\n$timestamp\n$scope\n${sha1(canonicalRequest)}"
        val signature = hmacSha1(credentials["secretKey"], stringToSign)
        return algorithm + " Credential=" + credentials["accessKey"] + "/" + scope + ", SignedHeaders=" + signedHeaders + ", Signature=" + signature
    }
}

