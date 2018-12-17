package ru.focus.zavalishina.eventchecker.data.web

import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import ru.focus.zavalishina.eventchecker.data.web.model.ConfirmationBody
import ru.focus.zavalishina.eventchecker.data.web.model.ConfirmationResponse
import ru.focus.zavalishina.eventchecker.data.web.model.EventBody
import ru.focus.zavalishina.eventchecker.data.web.model.GuestBody

interface Api {
    @GET("/api/v1/Events/registration")
    fun getEvents(): Single<List<EventBody>>

    @GET(" /api/v1/Registration/members/event/{eventId}?token=cftteamtest2018")
    fun getGuests(@Path("eventId") eventId: Int): Single<List<GuestBody>>

    @POST("/api/v1/Registration/members/event/{eventId}/confirmation?token=cftteamtest2018")
    fun confirm(@Path("eventId") eventId: Int, @Body confirmations: List<ConfirmationBody>) : Single<ConfirmationResponse>
}