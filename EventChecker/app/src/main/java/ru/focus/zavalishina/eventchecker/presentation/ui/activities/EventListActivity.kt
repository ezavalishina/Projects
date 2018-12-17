package ru.focus.zavalishina.eventchecker.presentation.ui.activities

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import ru.focus.zavalishina.eventchecker.R
import ru.focus.zavalishina.eventchecker.presentation.model.EventsList
import ru.focus.zavalishina.eventchecker.presentation.model.PresentationEvent
import ru.focus.zavalishina.eventchecker.presentation.presenters.EventListPresenter
import ru.focus.zavalishina.eventchecker.presentation.receivers.EventsBroadcastReceiver
import ru.focus.zavalishina.eventchecker.presentation.ui.services.LoadEventService

class EventListActivity : AppCompatActivity() {
    private lateinit var presenter: EventListPresenter
    private lateinit var eventsBroadcastReceiver: EventsBroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_list)
        presenter = EventListPresenter(this)
        presenter.getEvents()
        eventsBroadcastReceiver = EventsBroadcastReceiver(presenter, findViewById(R.id.events_list), this)
        registerReceiver(eventsBroadcastReceiver, IntentFilter(KEY_BROADCAST_EVENTS))


    }

    companion object {
        private const val KEY_BROADCAST_EVENTS = "ru.focus.zavalishina.eventchecker.presentation.ui.activities.KEY_BROADCAST_EVENTS"
        private const val KEY_EVENTS = "ru.focus.zavalishina.eventchecker.presentation.ui.activities.KEY_EVENTS"

        fun createIntentLoaderEventsService(context: Context) = Intent(context, LoadEventService::class.java)

        fun createIntentToBroadcastEvents(events: List<PresentationEvent>): Intent {
            return Intent(KEY_BROADCAST_EVENTS)
                    .putExtra(KEY_EVENTS, EventsList(events))
        }

        fun getEvents(intent: Intent): List<PresentationEvent> {
            val eventsList = intent.getSerializableExtra(KEY_EVENTS)
            return if (eventsList is EventsList)
                eventsList.events
            else listOf()
        }
    }
}
