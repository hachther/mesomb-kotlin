package com.hachther.mesomb.models

import org.json.simple.JSONArray
import org.json.simple.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class Transaction(obj: JSONObject) {
    val pk: String
    val status: String
    val type: String
    val amount: Double
    val fees: Double
    val b_party: String
    var message: String? = null
    val service: String
    var reference: String? = null
    var ts: Date? = null
    val country: String
    val currency: String
    var fin_trx_id: String? = null
    var trxamount: Double? = null
    var customer: Customer? = null
    var location: Location? = null
    var products: Array<Product?>? = null

    init {
        pk = obj["pk"] as String
        status = obj["status"] as String
        type = obj["type"] as String
        amount = if (obj["amount"] is Long) {
            (obj["amount"] as Long).toDouble()
        } else {
            obj["amount"] as Double
        }
        fees = if (obj["fees"] is Long) {
            (obj["fees"] as Long).toDouble()
        } else {
            obj["fees"] as Double
        }
        b_party = obj["b_party"] as String
        if (obj.getOrDefault("message", null) != null) {
            message = obj["message"] as String
        }
        service = obj["service"] as String
        if (obj.getOrDefault("reference", null) != null) {
            reference = obj["reference"] as String
        }
        try {
            ts = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX").parse(obj.get("ts") as String)
        } catch (ignored: Exception) {
        }
        country = obj["country"] as String
        currency = obj["currency"] as String
        if (obj.getOrDefault("fin_trx_id", null) != null) {
            fin_trx_id = obj["fin_trx_id"] as String
        }
        if (obj.getOrDefault("trxamount", null) != null) {
            trxamount = if (obj["trxamount"] is Long) {
                (obj["trxamount"] as Long).toDouble()
            } else {
                obj["trxamount"] as Double
            }
        }
        if (obj.getOrDefault("customer", null) != null) {
            customer = Customer(obj["customer"] as JSONObject)
        }
        if (obj.getOrDefault("location", null) != null) {
            location = Location(obj["location"] as JSONObject)
        }
        if (obj.getOrDefault("products", null) != null) {
            val prods = obj.get("products") as JSONArray
            products = arrayOfNulls(prods.size)
            for (i in prods.indices) {
                products!![i] = Product((prods[i] as JSONObject?)!!)
            }
        }
    }
}