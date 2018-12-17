package ru.focus.zavalishina.eventchecker.data.dataBase.entities

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class EventEntity(
        @PrimaryKey
        val id: Int?,
        val title: String?,
        val cities: List<CityEntity>?,
        val description: String?,
        val format: Int?,
        @Embedded
        val date: DateEntity?,
        val cardImage: String?,
        val status: Int?,
        val iconStatus: String?,
        val eventFormat: String?,
        val eventFormatEng: String?
)




