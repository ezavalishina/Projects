package ru.focus.zavalishina.rssreader.view.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;

import ru.focus.zavalishina.rssreader.model.structures.ChannelInfo;
import ru.focus.zavalishina.rssreader.model.DataBaseHelper;
import ru.focus.zavalishina.rssreader.view.activities.MainActivity;

public final class ChannelLoaderService extends IntentService {

    public ChannelLoaderService() {
        super("ChannelLoaderService");
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        final DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
        final ArrayList<ChannelInfo> channelInfos = dataBaseHelper.getChannelInfos();

        if (channelInfos != null) {
            final Intent newsIntent = MainActivity.createArrayChannelInfoIntent(channelInfos);
            LocalBroadcastManager.getInstance(this).sendBroadcast(newsIntent);
        }
    }

    public static Intent createIntent(final Context context) {
        return new Intent(context, ChannelLoaderService.class);
    }
}
