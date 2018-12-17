package ru.focus.zavalishina.eventchecker.presentation.ui.adapters

import android.os.Build
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ru.focus.zavalishina.eventchecker.R
import ru.focus.zavalishina.eventchecker.presentation.model.PresentationEvent
import ru.focus.zavalishina.eventchecker.presentation.presenters.EventListPresenter

class EventAdapter(private val layoutInflater: LayoutInflater,
                   private val eventListPresenter: EventListPresenter,
                   private val events: List<PresentationEvent>) : RecyclerView.Adapter<EventHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventHolder {
        val view = layoutInflater.inflate(R.layout.event_list_item, parent,false)
        return EventHolder(eventListPresenter, view, events)
    }

    override fun getItemCount() = events.size

    override fun onBindViewHolder(holder: EventHolder, position: Int) {
        holder.changeEvent(position)
    }
}

class EventHolder(eventListPresenter: EventListPresenter, view: View,
                          private val events: List<PresentationEvent>) : RecyclerView.ViewHolder(view) {
    private val eventName = view.findViewById<TextView>(R.id.event_name_textView)
    private val eventDate = view.findViewById<TextView>(R.id.event_date_textView)
    private val eventPlace = view.findViewById<TextView>(R.id.event_place_textView)
    private val eventTime = view.findViewById<TextView>(R.id.event_place_textView)
    private val eventGuestNumber = view.findViewById<TextView>(R.id.event_guests_number_textView)
    private var positionInRecycler = 0

    init {
        view.setOnClickListener {
            //eventListPresenter.moveFormEvent(events, positionInRecycler)
        }
    }

    @Suppress("DEPRECATION")
    fun changeEvent(position: Int){
        positionInRecycler = position
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            eventName.text = Html.fromHtml(events[position].title ?: "", Html.FROM_HTML_MODE_LEGACY)
        } else {
            eventName.text = Html.fromHtml(events[position].title ?: "")
        }

        eventPlace.text = events[position].cities?.get(0)?.nameRus ?: ""
        //eventTime.text = events[position].getStartDate()
        eventDate.text = events[position].date?.start ?: ""
    }
}