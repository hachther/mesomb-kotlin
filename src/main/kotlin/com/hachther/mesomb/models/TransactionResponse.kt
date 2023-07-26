package com.hachther.mesomb.models

import org.json.simple.JSONObject


class TransactionResponse(obj: JSONObject) {
    val success: Boolean;
    val message: String?
    val redirect: String?
    val transaction: Transaction?
    val reference: String?
    val status: String

    init {
        success = obj["success"] as Boolean
        message = obj["message"] as String?
        redirect = obj["redirect"] as String?
        transaction = Transaction(obj["transaction"] as JSONObject);
        reference = obj["reference"] as String?
        status = obj["status"] as String
    }

    fun isOperationSuccess(): Boolean {
        return success
    }

    fun isTransactionSuccess(): Boolean {
        return success && status == "SUCCESS"
    }
}