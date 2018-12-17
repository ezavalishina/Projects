package ru.focus.zavalishina.eventchecker.domain

import io.reactivex.Single
import ru.focus.zavalishina.eventchecker.domain.model.Event
import ru.focus.zavalishina.eventchecker.domain.model.Guest

interface Repository {
    fun getEvents(): Single<List<Event>>
    fun getActualEvents(): Single<List<Event>>
    fun sendConfirmations(eventId: Int, guests: List<Guest>)
    fun getCountGuests(): Int
    fun resendConfirmations()
}