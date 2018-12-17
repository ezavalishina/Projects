package ru.focus.zavalishina.eventchecker.domain

import android.content.Context
import ru.focus.zavalishina.eventchecker.data.repository.RepositoryEvents

object RepositoryFactory {
    fun create(context: Context) : Repository = RepositoryEvents(context)
}