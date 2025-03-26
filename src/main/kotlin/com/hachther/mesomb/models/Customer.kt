package com.hachther.mesomb.models

import org.json.JSONObject

class Customer(obj: JSONObject) {
    var email: String? = obj.optString("email", null)
    var phone: String? = obj.optString("phone", null)
    var town: String? = obj.optString("town", null)
    var region: String? = obj.optString("region", null)
    var country: String? = obj.optString("country", null)
    var firstName: String? = obj.optString("first_name", null)
    var lastName: String? = obj.optString("last_name", null)
    var address: String? = obj.optString("address", null)
}