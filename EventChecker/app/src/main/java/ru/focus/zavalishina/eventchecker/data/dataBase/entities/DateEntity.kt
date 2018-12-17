package ru.focus.zavalishina.eventchecker.data.dataBase.entities

import android.arch.persistence.room.PrimaryKey

data class DateEntity(
        val start: String?,
        val end: String?
)
{
    @PrimaryKey(autoGenerate = true)
    var dateId : Long? = null
}