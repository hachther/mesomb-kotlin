package com.hachther.mesomb.models

import org.json.JSONObject

class Product(obj: JSONObject) {
    val id: String = obj.getString("id")
    val name: String = obj.getString("name")
    var category: String? = obj.optString("category", null)
    var quantity: Int? = if (obj.has("quantity")) obj.getInt("quantity") else null
    var amount: Double? = if (obj.has("double")) obj.getDouble("amount") else null
}