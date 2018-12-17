package ru.focus.zavalishina.eventchecker.data.dataBase.dao

import android.arch.persistence.room.*
import ru.focus.zavalishina.eventchecker.data.dataBase.entities.CityEntity

@Dao
interface CityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg cities: CityEntity)

    @Delete
    fun delete(city: CityEntity)

    @Query("DELETE FROM CityEntity")
    fun deleteAll()

    @Query("SELECT * FROM CityEntity")
    fun getAllCities(): List<CityEntity>

    @Query("SELECT COUNT(*) FROM CityEntity")
    fun getCountCities(): Int
}