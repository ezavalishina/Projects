package ru.focus.zavalishina.eventchecker.data.web

import ru.focus.zavalishina.eventchecker.data.web.model.ConfirmationBody

class WebDataSource() {
    private val api: Api

    init {
        val retrofit = RetrofitBuilder.build()
        api = retrofit.create(Api::class.java)
    }

    fun getEvents() = api.getEvents()
    fun getGuests(eventId: Int) = api.getGuests(eventId)
    fun sendConfirms(eventId: Int, confirmations: List<ConfirmationBody>) = api.confirm(eventId, confirmations)
}