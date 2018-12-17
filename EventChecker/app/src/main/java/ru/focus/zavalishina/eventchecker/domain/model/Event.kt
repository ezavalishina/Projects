package ru.focus.zavalishina.eventchecker.domain.model

data class Event(
        val id: Int?,
        val title: String?,
        val cities: List<City>?,
        val description: String?,
        val format: Int?,
        val date: Date?,
        val cardImage: String?,
        val status: Int?,
        val iconStatus: String?,
        val eventFormat: String?,
        val eventFormatEng: String?
)