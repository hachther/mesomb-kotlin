package com.hachther.mesomb.models

import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date

abstract class ATransaction(data: JSONObject) {
    private var _data = data;

    var pk: String = data.getString("pk")
    var status: String = data.getString("status")
    var type: String = data.getString("type")
    var amount: Double = data.getDouble("amount")
    var fees: Double? = if (data.has("fees")) data.getDouble("fees") else null
    var bParty: String = data.getString("b_party")
    var message: String? = data.optString("message", null)
    var service: String = data.getString("service")
    var reference: String? = data.optString("reference", null)
    var date: Date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(data.getString("ts"))
    var country: String = data.getString("country")
    var currency: String = data.getString("currency")
    var finTrxId: String? = data.optString("fin_trx_id", null)
    var trxamount: Double? = if (data.has("trxamount")) data.getDouble("trxamount") else null
    var location: Location? = if (data.optJSONObject("location", null) != null) Location(data.getJSONObject("location")) else null

    fun isSuccessful(): Boolean {
        return status == "SUCCESS"
    }

    fun isFailed(): Boolean {
        return status == "FAILED"
    }

    fun isPending(): Boolean {
        return status == "PENDING"
    }

    fun getData(): JSONObject {
        return _data
    }
}