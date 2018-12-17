package ru.focus.zavalishina.eventchecker.presentation.ui.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.focus.zavalishina.eventchecker.R
import ru.focus.zavalishina.eventchecker.domain.Iteractor
import ru.focus.zavalishina.eventchecker.presentation.model.Converter
import ru.focus.zavalishina.eventchecker.presentation.model.EventsList
import ru.focus.zavalishina.eventchecker.presentation.model.PresentationEvent
import ru.focus.zavalishina.eventchecker.presentation.presenters.EventListPresenter
import ru.focus.zavalishina.eventchecker.presentation.receivers.EventsBroadcastReceiver
import ru.focus.zavalishina.eventchecker.presentation.ui.adapters.EventAdapter
import java.sql.SQLOutput

class EventListActivity : AppCompatActivity() {
    private lateinit var presenter: EventListPresenter
    private lateinit var eventsBroadcastReceiver: EventsBroadcastReceiver
    private lateinit var events : List<PresentationEvent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_list)
        presenter = EventListPresenter(this)
        val iteractor = Iteractor(this).getEvents()
        iteractor.map { event -> Converter.toPresentationEventsList(event) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            this@EventListActivity.events = it
                            findViewById<RecyclerView>(R.id.events_list).apply {
                            layoutManager = LinearLayoutManager(this@EventListActivity)
                            this.adapter = EventAdapter(LayoutInflater.from(this@EventListActivity), presenter, it) }
                        },
                        {
                            _ ->
                        })
        }

    companion object {
        private const val KEY_BROADCAST_EVENTS = "ru.focus.zavalishina.eventchecker.presentation.ui.activities.KEY_BROADCAST_EVENTS"
        private const val KEY_EVENTS = "ru.focus.zavalishina.eventchecker.presentation.ui.activities.KEY_EVENTS"

        //fun createIntentLoaderEventsService(context: Context) = Intent(context, LoadEventService::class.java)

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
