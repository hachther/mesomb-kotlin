package com.hachther.mesomb.models

import org.json.simple.JSONArray
import org.json.simple.JSONObject


class Application(obj: JSONObject) {
    var key: String? = null
    var logo: String? = null
    var balances: JSONArray? = null
    var countries: JSONArray? = null
    var description: String? = null
    var isLive = false
    var name: String? = null
    var security: JSONObject? = null
    var status: String? = null
    var url: String? = null

    init {
        key = obj["key"] as String?
        logo = obj["logo"] as String?
        balances = obj["balances"] as JSONArray?
        countries = obj["countries"] as JSONArray?
        description = obj["description"] as String?
        isLive = obj["is_live"] as Boolean
        name = obj["name"] as String?
        security = obj["security"] as JSONObject?
        status = obj["status"] as String?
        url = obj["url"] as String?
    }

    fun getBalance(country: String?, service: String?): Float {
        var balance = 0f
        for (o in balances!!) {
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
    fun getSecurityField(field: String?): Any? {
        return security!![field]
    }
}