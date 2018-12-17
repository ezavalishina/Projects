package ru.focus.zavalishina.eventchecker.data.repository

import android.content.Context
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import ru.focus.zavalishina.eventchecker.domain.Repository
import ru.focus.zavalishina.eventchecker.data.dataBase.AppDatabase
import ru.focus.zavalishina.eventchecker.domain.model.Event
import ru.focus.zavalishina.eventchecker.domain.model.Guest
import ru.focus.zavalishina.eventchecker.data.web.WebDataSource

class RepositoryEvents(context: Context) : Repository {
    private val webDataSourse = WebDataSource()
    private val guestDao = AppDatabase.getInstance(context).guestDao()
    private val eventDao = AppDatabase.getInstance(context).eventDao()

    override fun getEvents(): Single<List<Event>> {

        val haveAnkets = eventDao.getCountEvents() != 0

        return if (haveAnkets) {
            Single.just(eventDao.getAllEvents()).map { RepositoryConverter.toEventsListFromEntity(it) }
        } else {
            return getActualEvents()
        }
    }

    override fun getActualEvents() = webDataSourse.getEvents()
            .map { events ->
                eventDao.deleteAll()
                for (event in RepositoryConverter.toEventEntitysListFromAnswer(events)) {
                    eventDao.insertAll(event)
                }
                return@map events
            }
            .map { events -> RepositoryConverter.toEventsList(events) }
            .subscribeOn(Schedulers.io())

    override fun sendConfirmations(eventId: Int, guests: List<Guest>) {
        val forSendConfirmationBody = RepositoryConverter.toConfirmationBodyList(guests)
        val response = webDataSourse.sendConfirms(eventId, forSendConfirmationBody)
    }

    override fun getCountGuests() = guestDao.getCountGuests()

    override fun resendConfirmations() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}