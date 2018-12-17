package ru.focus.zavalishina.eventchecker.presentation.model

import android.app.Presentation
import ru.focus.zavalishina.eventchecker.domain.model.City
import ru.focus.zavalishina.eventchecker.domain.model.Date
import ru.focus.zavalishina.eventchecker.domain.model.Event
import java.text.SimpleDateFormat
import java.util.*

object Converter {
    fun toPresentationEventsList(events: List<Event>) : List<PresentationEvent> {
        val convertEvents = mutableListOf<PresentationEvent>()
        for (event in events) {
            convertEvents.add(toPresentationEvent(event))
        }
        return convertEvents.toList()
    }

    fun toPresentationEvent(event: Event) = PresentationEvent(
            event.id,
            event.title,
            toPresentationCityList(event.cities),
            event.description,
            event.format,
            toPresentationDate(event.date),
            event.cardImage,
            event.status,
            event.iconStatus,
            event.eventFormat,
            event.eventFormatEng)

    private fun toPresentationCityList(cities : List<City>?) : List<PresentationCity>? {
        if (cities == null) return null
        val list = mutableListOf<PresentationCity>()
        for (city in cities) {
            list.add(toPresentationCity(city))
        }
        return list.toList()
    }

    private fun toPresentationCity(city: City) = PresentationCity(
            city.id,
            city.nameRus,
            city.nameEng,
            city.icon,
            city.isActive
    )

    private fun toPresentationDate(date: Date?): PresentationDate? = if (date == null) null
    else PresentationDate(date.start,
            date.end)
}