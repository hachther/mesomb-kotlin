package com.hachther.mesomb.models

import org.json.JSONObject

class ContributionResponse(data: JSONObject) {
    val isOperationSuccess: Boolean = data.getBoolean("success")
    val message: String? = data.optString("message", null)
    val contribution: Contribution = Contribution(data.getJSONObject("contribution"))
    val status: String = data.getString("status")

    val isContributionSuccess: Boolean
        get() = this.contribution.isSuccessful()
}