package com.hachther.mesomb.models

import org.json.simple.JSONArray
import org.json.simple.JSONObject


class Application(obj: JSONObject) {
    private var key: String? = null
    private var logo: String? = null
    private var balances: JSONArray? = null
    private var countries: JSONArray? = null
    private var description: String? = null
    private var isLive = false
    private var name: String? = null
    private var security: JSONObject? = null
    private var status: String? = null
    private var url: String? = null

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

    fun getKey(): String? {
        return key
    }

    fun getLogo(): String? {
        return logo
    }

    fun getCountries(): JSONArray? {
        return countries
    }

    fun getDescription(): String? {
        return description
    }

    fun isLive(): Boolean {
        return isLive
    }

    fun getName(): String? {
        return name
    }

    fun getStatus(): String? {
        return status
    }

    fun getUrl(): String? {
        return url
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

    fun getBalance(): Float {
        return this.getBalance(null, null)
    }

    fun getBalance(country: String?): Float {
        return this.getBalance(country, null)
    }

    fun getSecurityField(field: String?): Any? {
        return security!![field]
    }
}