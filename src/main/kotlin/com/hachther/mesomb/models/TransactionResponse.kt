package com.hachther.mesomb.models

import org.json.simple.JSONObject


class TransactionResponse(obj: JSONObject) {
    private var success = false
    private var message: String? = null
    private var redirect: String? = null
    private var data: Map<String, Any>? = null
    private var reference: String? = null
    private var status: String? = null

    init {
        success = obj["success"] as Boolean
        message = obj["message"] as String?
        redirect = obj["redirect"] as String?
        data = obj["data"] as Map<String, Any>?
        reference = obj["reference"] as String?
        status = obj["status"] as String?
    }

    fun isSuccess(): Boolean {
        return success
    }

    fun getMessage(): String? {
        return message
    }

    fun getRedirect(): String? {
        return redirect
    }

    fun getData(): Map<String, Any>? {
        return data
    }

    fun getReference(): String? {
        return reference
    }

    fun getStatus(): String? {
        return status
    }

    fun isOperationSuccess(): Boolean {
        return success
    }

    fun isTransactionSuccess(): Boolean {
        return success && status == "SUCCESS"
    }
}