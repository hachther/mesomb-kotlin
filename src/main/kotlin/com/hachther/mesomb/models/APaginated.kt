package com.hachther.mesomb.models

import org.json.JSONObject

abstract class APaginated(data: JSONObject) {
    var count: Int = data.getInt("count")
    var next: String? = data.optString("next", null)
    var previous: String? = data.optString("previous", null)
}