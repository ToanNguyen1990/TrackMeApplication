package net.toannt.hacore.api

interface HMRestParseException {
    fun getErrorMessage(responseBody: String): String?
}