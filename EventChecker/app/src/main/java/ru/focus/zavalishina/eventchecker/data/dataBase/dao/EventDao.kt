package ru.focus.zavalishina.eventchecker.data.dataBase.dao

import android.arch.persistence.room.*
import ru.focus.zavalishina.eventchecker.data.dataBase.entities.EventEntity

@Dao
interface EventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg events: EventEntity)

    @Delete
    fun delete(event: EventEntity)

    @Query("DELETE FROM EventEntity")
    fun deleteAll()

    @Query("SELECT * FROM EventEntity")
    fun getAllEvents() : List<EventEntity>

    @Query("SELECT COUNT(*) FROM EventEntity")
    fun getCountEvents() : Int
}