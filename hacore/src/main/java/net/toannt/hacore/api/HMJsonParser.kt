package net.toannt.hacore.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder

object HMJsonParser {

    fun getDefaultGSONParser(): Gson {
        val builder = GsonBuilder()
            .setLenient()
            .serializeNulls()

        return builder.create()
    }
}