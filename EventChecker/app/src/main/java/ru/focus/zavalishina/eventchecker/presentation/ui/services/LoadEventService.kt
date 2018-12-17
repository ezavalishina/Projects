package ru.focus.zavalishina.eventchecker.presentation.ui.services

import android.app.IntentService
import android.content.Intent
import io.reactivex.disposables.Disposable
import ru.focus.zavalishina.eventchecker.domain.Interrupter
import ru.focus.zavalishina.eventchecker.presentation.model.Converter
import ru.focus.zavalishina.eventchecker.presentation.ui.activities.EventListActivity

class LoadEventService : IntentService("ru.focus.zavalishina.eventchecker.presentation.ui.services.LoadEventService") {
    private val interrupter = Interrupter(this)
    private lateinit var disposable: Disposable

    override fun onHandleIntent(intent: Intent?) {
        if (intent == null) {
            return
        }

        disposable = interrupter.getEvents()
                .map { event -> Converter.toPresentationEventsList(event) }
                .subscribe(
                        { events -> run {
                                sendBroadcast(
                                        EventListActivity.createIntentToBroadcastEvents(events))
                            }
                        },
                        {_ ->}
                )
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }
}