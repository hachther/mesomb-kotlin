package com.hachther.mesomb.models

import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date

class WalletTransaction(data: JSONObject) {
    var id: Long = data.getLong("id")
    var status: String = data.getString("status")
    var type: String = data.getString("type")
    var amount: Double = data.getDouble("amount")
    var direction: Int = data.getInt("direction")
    var wallet: Long = data.getLong("wallet")
    var balanceAfter: Double = data.optDouble("balance_after")
    var date: Date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(
        data.getString("date")
    )
    var country: String = data.getString("country")
    var finTrxId: String = data.getString("fin_trx_id")
}