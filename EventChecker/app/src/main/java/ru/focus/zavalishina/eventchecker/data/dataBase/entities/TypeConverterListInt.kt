package ru.focus.zavalishina.eventchecker.data.dataBase.entities

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TypeConverterListInt {
    @TypeConverter
    fun fromInt(cities: List<Int>?): String? {
        val gson = Gson()
        return gson.toJson(cities)
    }

    @TypeConverter
    fun toInt(json: String?): List<Int>? {
        val listType = object : TypeToken<List<Int>>() {}.type
        return Gson().fromJson<List<Int>>(json, listType)
    }
}