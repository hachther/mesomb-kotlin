package com.hachther.mesomb.models

import org.json.JSONArray
import org.json.JSONObject

class Application(obj: JSONObject) {
    var key: String = obj.getString("key")
    var logo: String? = obj.optString("logo", null)
    var balances: JSONArray = obj.getJSONArray("balances")
    var countries: List<String> = obj.getJSONArray("countries").map { it.toString() }
    var description: String? = obj.optString("description", null)
    var name: String = obj.getString("name")
    var url: String? = obj.optString("url")

    fun getBalance(country: String?, service: String?): Float {
        var balance = 0f
        for (o in balances) {
            val bal = o as JSONObject
            if (country != null && bal["country"] !== country) {
                continue
            }
            if (service != null && bal["service"] !== service) {
                continue
            }
            balance += bal["value"] as Float
        }
        return balance
    }
}