package ru.focus.zavalishina.eventchecker.presentation.model

import java.io.Serializable

data class PresentationDate(
        val start: String?,
        val end: String?
) : Serializable