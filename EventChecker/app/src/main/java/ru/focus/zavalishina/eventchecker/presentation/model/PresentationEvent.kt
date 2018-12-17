package ru.focus.zavalishina.eventchecker.presentation.model

import java.io.Serializable

data class PresentationEvent(
        val id: Int?,
        val title: String?,
        val cities: List<PresentationCity>?,
        val description: String?,
        val format: Int?,
        val date: PresentationDate?,
        val cardImage: String?,
        val status: Int?,
        val iconStatus: String?,
        val eventFormat: String?,
        val eventFormatEng: String?
) : Serializable