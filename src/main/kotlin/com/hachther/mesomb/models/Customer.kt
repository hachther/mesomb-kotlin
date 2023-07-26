package com.hachther.mesomb.models

import org.json.simple.JSONObject

class Customer(obj: JSONObject) {
    var email: String? = null
    var phone: String? = null
    var town: String? = null
    var region: String? = null
    var country: String? = null
    var first_name: String? = null
    var last_name: String? = null
    var address: String? = null

    init {
        if (obj.getOrDefault("email", null) != null) {
            email = obj["email"].toString()
        }
        if (obj.getOrDefault("phone", null) != null) {
            phone = obj["phone"].toString()
        }
        if (obj.getOrDefault("town", null) != null) {
            town = obj["town"].toString()
        }
        if (obj.getOrDefault("region", null) != null) {
            region = obj["region"].toString()
        }
        if (obj.getOrDefault("country", null) != null) {
            country = obj["country"].toString()
        }
        if (obj.getOrDefault("first_name", null) != null) {
            first_name = obj["first_name"].toString()
        }
        if (obj.getOrDefault("last_name", null) != null) {
            last_name = obj["last_name"].toString()
        }
        if (obj.getOrDefault("address", null) != null) {
            address = obj["address"].toString()
        }
    }
}