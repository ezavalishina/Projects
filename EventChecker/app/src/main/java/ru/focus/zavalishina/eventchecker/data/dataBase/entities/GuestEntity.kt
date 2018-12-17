package ru.focus.zavalishina.eventchecker.data.dataBase.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class GuestEntity(
        @PrimaryKey
        val id: Int,
        val phone: String,
        val city: String,
        val company: String,
        val position: String,
        val addition: String,
        val registeredDate: String,
        val agreedByManager: String,
        val lastName: String,
        val firstName: String,
        val patronymic: String,
        val email: String
)