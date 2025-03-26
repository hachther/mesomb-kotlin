package com.hachther.mesomb.models

import org.json.JSONObject

class Transaction(data: JSONObject): ATransaction(data) {
    var customer: Customer? = if (data.optString("customer", null) != null) Customer(data.getJSONObject("customer")) else null
    var products: List<Product> = if (data.has("products")) data.optJSONArray("products").map { Product(it as JSONObject) } else emptyList()
}