package net.nvtoan.trackme.app.db.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object DataConverter {

    @TypeConverter
    @JvmStatic
    fun convertStringToList(data: String?): List<String>? {
        val type = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(data, type)
    }

    @TypeConverter
    @JvmStatic
    fun convertListToString(locations: List<String>?): String? {
        return Gson().toJson(locations)
    }

    @TypeConverter
    @JvmStatic
    fun convertStringToFloatList(data: String?): List<Float>? {
        val type = object : TypeToken<List<Float>>() {}.type
        return Gson().fromJson(data, type)
    }

    @TypeConverter
    @JvmStatic
    fun convertFloatListToString(locations: List<Float>?): String? {
        return Gson().toJson(locations)
    }
}