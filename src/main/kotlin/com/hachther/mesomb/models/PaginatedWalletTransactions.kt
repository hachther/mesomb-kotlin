package com.hachther.mesomb.models

import org.json.JSONArray
import org.json.JSONObject

class PaginatedWalletTransactions(data: JSONObject) : APaginated(data) {
    var results: List<WalletTransaction?>? = null

    init {
        this.results = data.getJSONArray("results").map { WalletTransaction(it as JSONObject) }
    }
}