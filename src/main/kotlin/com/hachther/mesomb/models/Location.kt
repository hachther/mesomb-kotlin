package com.hachther.mesomb.models

import org.json.JSONObject

class Location(obj: JSONObject) {
    val town: String = obj.getString("town")
    var region: String? = obj.optString("region")
    var country: String? = obj.optString("country")
}