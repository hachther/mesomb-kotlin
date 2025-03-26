package com.hachther.mesomb.models

import org.json.JSONObject

class PaginatedWallets(data: JSONObject) : APaginated(data) {
    var results: List<Wallet?>? = null

    init {
        this.results = data.getJSONArray("results").map { Wallet(it as JSONObject) }
    }
}