package ru.focus.zavalishina.eventchecker.domain

import android.content.Context
import ru.focus.zavalishina.eventchecker.domain.model.Guest

class Iteractor(context: Context){
    private val repository = RepositoryFactory.create(context)

    fun getEvents() = repository.getEvents()
    fun getActualEvents() = repository.getActualEvents()
    fun sendConfirmations(eventId: Int, guests: List<Guest>) = repository.sendConfirmations(eventId, guests)
    fun getCountGuests() = repository.getCountGuests()
    fun resendConfirmations() = repository.resendConfirmations()
}