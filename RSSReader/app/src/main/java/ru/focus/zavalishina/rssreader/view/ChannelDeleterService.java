package ru.focus.zavalishina.rssreader.view;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import ru.focus.zavalishina.rssreader.model.ChannelInfo;
import ru.focus.zavalishina.rssreader.model.DataBaseHelper;
import ru.focus.zavalishina.rssreader.view.activities.MainActivity;

public final class ChannelDeleterService extends IntentService {
    private static final String DELETER_CHANNEL_INFO_INTENT = "ru.focus.zavalishina.rssreader.DELETER_CHANNEL_INFO_INTENT";

    public ChannelDeleterService() {
        super("ChannelLoaderService");
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        final DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
        final ChannelInfo channelInfo = getChannelInfo(intent);

        if (channelInfo != null) {
            dataBaseHelper.deleteChannel(channelInfo);
            final Intent deleteIntent = MainActivity.createDeleteChannelInfoIntent(channelInfo);
            LocalBroadcastManager.getInstance(this).sendBroadcast(deleteIntent);
        }
    }

    private ChannelInfo getChannelInfo(Intent intent) {
        return (ChannelInfo) intent.getSerializableExtra(DELETER_CHANNEL_INFO_INTENT);
    }

    public static Intent createIntent(final Context context, final ChannelInfo channelInfo) {
        final Intent intent = new Intent(context, ChannelDeleterService.class);
        intent.putExtra(DELETER_CHANNEL_INFO_INTENT, channelInfo);
        return intent;
    }
}