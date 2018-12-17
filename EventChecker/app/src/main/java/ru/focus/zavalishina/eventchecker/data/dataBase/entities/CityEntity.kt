package ru.focus.zavalishina.eventchecker.data.dataBase.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class CityEntity(
        @PrimaryKey
        val id: Int?,
        val nameRus: String?,
        val nameEng: String?,
        val icon: String?,
        val isActive: Boolean?
)