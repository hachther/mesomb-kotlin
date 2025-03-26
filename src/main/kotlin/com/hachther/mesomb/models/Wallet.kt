package com.hachther.mesomb.models

import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date

class Wallet(val data: JSONObject) {
    val id: Long = data.getLong("id")
    val number: String = data.getString("number")
    val country: String = data.getString("country")
    val status: String = data.getString("status")
    var lastActivity: Date? = if (data.optString("last_activity", null) != null) SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(data.getString("last_activity")) else null
    var balance: Double? = if (data.has("balance")) data.getDouble("balance") else null
    var firstName: String? = data.optString("first_name", null)
    val lastName: String = data.getString("last_name")
    var email: String? = data.optString("email", null)
    val phoneNumber: String = data.getString("phone_number")
    val gender: String = data.getString("gender")
}
