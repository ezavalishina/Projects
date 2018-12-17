package ru.focus.zavalishina.eventchecker.presentation.model

import java.io.Serializable

data class PresentationCity(
        val id: Int?,
        val nameRus: String?,
        val nameEng: String?,
        val icon: String?,
        val isActive: Boolean?
) : Serializable