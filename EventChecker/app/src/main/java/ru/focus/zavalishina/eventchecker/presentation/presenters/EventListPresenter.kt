package ru.focus.zavalishina.eventchecker.presentation.presenters

import android.app.Activity
import ru.focus.zavalishina.eventchecker.presentation.model.PresentationEvent
import ru.focus.zavalishina.eventchecker.presentation.ui.activities.EventListActivity

class EventListPresenter(private val activity: Activity) {
    private var events = listOf<PresentationEvent>()

    fun getEvents() {
        //val intent = EventListActivity.createIntentLoaderEventsService(activity)
        //activity.startService(intent)
    }

    fun setEvents(events: List<PresentationEvent>) {
        this.events = events
    }


}