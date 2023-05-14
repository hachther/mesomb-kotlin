package com.hachther.mesomb


object MeSomb {
    /** The MeSomb API key to be used for requests.  */
    var apiKey: String? = null

    /** The MeSomb client key to be used for Connect requests.  */
    var clientKey: String? = null

    /** he MeSomb secret key to be used for Connect requests.  */
    var secretKey: String? = null

    /** The base URL for the MeSomb API.  */
    var apiBase = "https://mesomb.hachther.com"

    /** The version of the MeSomb API to use for requests.  */
    var apiVersion = "v1.1"

    /** The application's information (name, version, URL)  */
    var appInfo: String? = null

    /** Algorithm to used for signature  */
    var algorithm = "HMAC-SHA1"

    /** Maximum number of request retries  */
    var maxNetworkRetries = 0

    /** Whether client telemetry is enabled. Defaults to true.  */
    var enableTelemetry = true

    /** Maximum delay between retries, in seconds  */
    private const val maxNetworkRetryDelay = 2.0

    /** Maximum delay between retries, in seconds, that will be respected from the MeSomb API  */
    private const val maxRetryAfter = 60.0

    /** Initial delay between retries, in seconds  */
    private const val initialNetworkRetryDelay = 0.5
}