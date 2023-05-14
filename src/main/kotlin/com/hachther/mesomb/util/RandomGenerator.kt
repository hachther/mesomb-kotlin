package com.hachther.mesomb.util


object RandomGenerator {
    /**
     * Generate a random string by the length
     *
     * @param length size of the nonce to generate
     * @return String
     */
    @JvmOverloads
    fun nonce(length: Int = 40): String {
        val characters = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
        val s = StringBuilder(length)
        var i: Int
        i = 0
        while (i < length) {
            val ch = (characters.length * Math.random()).toInt()
            s.append(characters[ch])
            i++
        }
        return s.toString()
    }
}