package com.hachther.mesomb.models

import org.json.JSONObject


class TransactionResponse(obj: JSONObject) {
    val success: Boolean = obj.getBoolean("success")
    val message: String? = if (obj.has("message")) obj.getString("message") else null
    val redirect: String? = if (obj.has("redirect")) obj.getString("redirect") else null
    val transaction: Transaction = Transaction(obj.getJSONObject("transaction"))
    val reference: String? = if (obj.has("reference")) obj.getString("reference") else null
    val status: String = obj.getString("status")

    fun isOperationSuccess(): Boolean {
        return success
    }

    fun isTransactionSuccess() : Boolean {
        return transaction.isSuccessful()
    }
}