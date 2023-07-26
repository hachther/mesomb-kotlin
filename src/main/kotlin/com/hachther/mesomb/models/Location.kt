package com.hachther.mesomb.models

import org.json.simple.JSONObject

class Location(obj: JSONObject) {
    val town: String
    var region: String? = null
    var country: String? = null

    init {
        town = obj["town"].toString()
        if (obj.getOrDefault("region", null) != null) {
            region = obj["region"].toString()
        }
        if (obj.getOrDefault("country", null) != null) {
            country = obj["country"].toString()
        }
    }
}