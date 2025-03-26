package com.hachther.mesomb.models

import org.json.JSONObject

class Contribution(data: JSONObject) : ATransaction(data) {
    var contributor: Customer? = if (data.optString("contributor", null) != null) Customer(data.getJSONObject("contributor")) else null
}