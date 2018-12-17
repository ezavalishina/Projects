package ru.focus.zavalishina.eventchecker.data.repository

import ru.focus.zavalishina.eventchecker.data.dataBase.entities.CityEntity
import ru.focus.zavalishina.eventchecker.data.dataBase.entities.DateEntity
import ru.focus.zavalishina.eventchecker.data.dataBase.entities.EventEntity
import ru.focus.zavalishina.eventchecker.data.dataBase.entities.GuestEntity
import ru.focus.zavalishina.eventchecker.domain.model.City
import ru.focus.zavalishina.eventchecker.domain.model.Date
import ru.focus.zavalishina.eventchecker.domain.model.Event
import ru.focus.zavalishina.eventchecker.domain.model.Guest
import ru.focus.zavalishina.eventchecker.data.web.model.*

object RepositoryConverter {
    fun toEventsList(events: List<EventBody>): List<Event> {
        val convertEvents = mutableListOf<Event>()
        for (event in events) {
            convertEvents.add(toEvent(event))
        }
        return convertEvents.toList()
    }

    fun toEventsListFromEntity(events: List<EventEntity>): List<Event> {
        val convertEvents = mutableListOf<Event>()
        for (event in events) {
            convertEvents.add(toEvent(event))
        }
        return convertEvents.toList()
    }

    fun toEventEntitysListFromAnswer(events: List<EventBody>): List<EventEntity> {
        val convertEvents = mutableListOf<EventEntity>()
        for (event in events) {
            convertEvents.add(toEventEntity(event))
        }
        return convertEvents.toList()
    }

    fun toConfirmationBodyList(guests: List<Guest>): List<ConfirmationBody> {
        val convertConfirmations = mutableListOf<ConfirmationBody>()
        for (guest in guests) {
            convertConfirmations.add(getConfirmationBody(guest))
        }

        return convertConfirmations.toList()
    }

    fun getConfirmationBody(guest: Guest) = ConfirmationBody(
            guest.id,
            true,
            java.util.Calendar.getInstance().toString()
    )

    fun toGuestBody(guest: Guest) = GuestBody(
            guest.id,
            guest.phone,
            guest.city,
            guest.company,
            guest.position,
            guest.addition,
            guest.registeredDate,
            guest.agreedByManager,
            guest.lastName,
            guest.firstName,
            guest.patronymic,
            guest.email
    )

    fun toGuestBody(guestEntity: GuestEntity) = GuestBody(
            guestEntity.id,
            guestEntity.phone,
            guestEntity.city,
            guestEntity.company,
            guestEntity.position,
            guestEntity.addition,
            guestEntity.registeredDate,
            guestEntity.agreedByManager,
            guestEntity.lastName,
            guestEntity.firstName,
            guestEntity.patronymic,
            guestEntity.email
    )

    fun toGuestEntity(guest: Guest) = GuestEntity(
            guest.id,
            guest.phone,
            guest.city,
            guest.company,
            guest.position,
            guest.addition,
            guest.registeredDate,
            guest.agreedByManager,
            guest.lastName,
            guest.firstName,
            guest.patronymic,
            guest.email
    )

    private fun toEvent(eventBody: EventBody) = Event(
            eventBody.id,
            eventBody.title,
            toCityList(eventBody.cities),
            eventBody.description,
            eventBody.format,
            toDate(eventBody.date),
            eventBody.cardImage,
            eventBody.status,
            eventBody.iconStatus,
            eventBody.eventFormat,
            eventBody.eventFormatEng)

    private fun toEvent(eventEntity: EventEntity) = Event(
            eventEntity.id,
            eventEntity.title,
            toCityListDb(eventEntity.cities),
            eventEntity.description,
            eventEntity.format,
            toDate(eventEntity.date),
            eventEntity.cardImage,
            eventEntity.status,
            eventEntity.iconStatus,
            eventEntity.eventFormat,
            eventEntity.eventFormatEng)

    private fun toEventEntity(eventBody: EventBody) = EventEntity(
            eventBody.id,
            eventBody.title,
            toCityEntityList(eventBody.cities),
            eventBody.description,
            eventBody.format,
            toDateEntity(eventBody.date),
            eventBody.cardImage,
            eventBody.status,
            eventBody.iconStatus,
            eventBody.eventFormat,
            eventBody.eventFormatEng)

    private fun toCityListDb(cities: List<CityEntity>?): List<City>? {
        if (cities == null) return null
        val list = mutableListOf<City>()
        for (city in cities) {
            list.add(toCity(city))
        }
        return list.toList()
    }

    private fun toCityList(cities: List<CityBody>?): List<City>? {
        if (cities == null) return null
        val list = mutableListOf<City>()
        for (city in cities) {
            list.add(toCity(city))
        }
        return list.toList()
    }

    private fun toCityEntityList(cities: List<CityBody>?): List<CityEntity>? {
        if (cities == null) return null
        val list = mutableListOf<CityEntity>()
        for (city in cities) {
            list.add(toCityEntity(city))
        }
        return list.toList()
    }


    private fun toCity(city: CityBody) = City(
            city.id,
            city.nameRus,
            city.nameEng,
            city.icon,
            city.isActive)

    private fun toCity(cityEntity: CityEntity) = City(
            cityEntity.id,
            cityEntity.nameRus,
            cityEntity.nameEng,
            cityEntity.icon,
            cityEntity.isActive)

    private fun toCityEntity(cityBody: CityBody) = CityEntity(
            cityBody.id,
            cityBody.nameRus,
            cityBody.nameEng,
            cityBody.icon,
            cityBody.isActive)

    private fun toDate(dateBody: DateBody?): Date? = if (dateBody == null) null
    else Date(dateBody.start,
            dateBody.end)

    private fun toDate(dateEntity: DateEntity?): Date? = if (dateEntity == null) null
    else Date(dateEntity.start,
            dateEntity.end)

    private fun toDateEntity(dateBody: DateBody?): DateEntity? = if (dateBody == null) null
    else DateEntity(dateBody.start,
            dateBody.end)

}