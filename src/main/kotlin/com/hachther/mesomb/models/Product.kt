package com.hachther.mesomb.models

import org.json.simple.JSONObject

class Product(obj: JSONObject) {
    val name: String
    var category: String? = null
    var quantity: Int? = null
    var amount: Float? = null

    init {
        name = obj["name"] as String
        if (obj.getOrDefault("category", null) != null) {
            category = obj["category"].toString()
        }
        if (obj.getOrDefault("quantity", null) != null) {
            quantity = obj["quantity"] as Int
        }
        if (obj.getOrDefault("email", null) != null) {
            amount = obj["amount"] as Float
        }
    }
}