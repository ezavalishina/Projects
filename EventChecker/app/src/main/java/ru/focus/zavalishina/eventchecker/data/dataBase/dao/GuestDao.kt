package ru.focus.zavalishina.eventchecker.data.dataBase.dao

import android.arch.persistence.room.*
import ru.focus.zavalishina.eventchecker.data.dataBase.entities.GuestEntity

@Dao
interface GuestDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg guests: GuestEntity)

    @Delete
    fun delete(guest: GuestEntity)

    @Query("DELETE FROM GuestEntity")
    fun deleteAll()

    @Query("SELECT * FROM GuestEntity")
    fun getAllGuests(): List<GuestEntity>

    @Query("SELECT COUNT(*) FROM GuestEntity")
    fun getCountGuests(): Int
}