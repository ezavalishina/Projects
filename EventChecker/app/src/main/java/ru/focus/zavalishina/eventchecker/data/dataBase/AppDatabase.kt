package ru.focus.zavalishina.eventchecker.data.dataBase

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import ru.focus.zavalishina.eventchecker.data.dataBase.dao.CityDao
import ru.focus.zavalishina.eventchecker.data.dataBase.dao.EventDao
import ru.focus.zavalishina.eventchecker.data.dataBase.dao.GuestDao
import ru.focus.zavalishina.eventchecker.data.dataBase.entities.*

@Database(
        entities = [
            (EventEntity::class),
            (CityEntity::class),
            (GuestEntity::class),
            (DateEntity::class)
        ],
        version = 1,
        exportSchema = false
)

@TypeConverters(
        TypeConverterCities::class,
        TypeConverterListInt::class
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
    abstract fun guestDao(): GuestDao
    abstract fun cityDao(): CityDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "appDataBase.db").allowMainThreadQueries().build()
            }
            return INSTANCE!!
        }

        fun destroyInstance() {
            INSTANCE = null
        }

    }
}