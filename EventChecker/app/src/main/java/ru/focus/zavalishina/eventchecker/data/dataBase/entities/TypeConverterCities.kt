package ru.focus.zavalishina.eventchecker.data.dataBase.entities

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TypeConverterCities {
    @TypeConverter
    fun fromCities(cities: List<CityEntity>?): String? {
        val gson = Gson()
        return gson.toJson(cities)
    }

    @TypeConverter
    fun toCities(json: String?): List<CityEntity>? {
        val listType = object : TypeToken<List<CityEntity>>() {}.type
        return Gson().fromJson<List<CityEntity>>(json, listType)
    }

}