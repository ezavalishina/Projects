package ru.focus.zavalishina.rssreader.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;

import ru.focus.zavalishina.rssreader.model.structures.ChannelInfo;
import ru.focus.zavalishina.rssreader.model.database.DataBaseHelper;
import ru.focus.zavalishina.rssreader.view.activities.MainActivity;

public final class ChannelDeleteService extends IntentService {
    private static final String DELETER_CHANNEL_INFO_INTENT = "ru.focus.zavalishina.rssreader.DELETER_CHANNEL_INFO_INTENT";

    public ChannelDeleteService() {
        super("ChannelLoadService");
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        if (intent == null) {
            return;
        }
        final DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
        final ChannelInfo channelInfo = getChannelInfo(intent);

        if (channelInfo != null) {
            dataBaseHelper.deleteChannel(channelInfo);
            final Intent deleteIntent = MainActivity.createDeleteChannelInfoIntent(channelInfo);
            LocalBroadcastManager.getInstance(this).sendBroadcast(deleteIntent);
        }
    }

    private ChannelInfo getChannelInfo(final @NonNull Intent intent) {
        return (ChannelInfo) intent.getSerializableExtra(DELETER_CHANNEL_INFO_INTENT);
    }

    public static Intent createIntent(final Context context, final @NonNull ChannelInfo channelInfo) {
        final Intent intent = new Intent(context, ChannelDeleteService.class);
        intent.putExtra(DELETER_CHANNEL_INFO_INTENT, channelInfo);
        return intent;
    }
}