package ru.focus.zavalishina.eventchecker.presentation.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import ru.focus.zavalishina.eventchecker.presentation.presenters.EventListPresenter
import ru.focus.zavalishina.eventchecker.presentation.ui.activities.EventListActivity
import ru.focus.zavalishina.eventchecker.presentation.ui.adapters.EventAdapter

class EventsBroadcastReceiver(private val presenter: EventListPresenter, private val recyclerView: RecyclerView, private val activity: EventListActivity): BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent == null) return
        val events = EventListActivity.getEvents(intent)
        presenter.setEvents(events)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            this.adapter = EventAdapter(LayoutInflater.from(activity), presenter, events)
        }
    }
}