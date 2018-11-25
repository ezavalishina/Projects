package ru.focus.zavalishina.rssreader.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import ru.focus.zavalishina.rssreader.model.DataBaseHelper;
import ru.focus.zavalishina.rssreader.model.NewsLoader;
import ru.focus.zavalishina.rssreader.model.structures.ChannelInfo;
import ru.focus.zavalishina.rssreader.view.activities.MainActivity;

public final class NewsUpdateService extends IntentService {

    public NewsUpdateService() {
        super("NewsLoadService");
    }
    private static final String URL_ARRAY = "ru.focus.zavalishina.rssreader.URL_ARRAY";

    @Override
    public void onHandleIntent(final Intent intent) {
        if (intent == null) {
            return;
        }
        final ArrayList<String> URLs = (ArrayList<String>) intent.getSerializableExtra(URL_ARRAY);
        if (URLs == null) {
            return;
        }
        for (String urlString : URLs) {
            final URL url = getUrl(urlString);
            if (url == null) {
                return;
            }
            final NewsLoader newsLoader = new NewsLoader();
            final ChannelInfo channelInfo = newsLoader.loadWithUrl(url);

            if (channelInfo != null) {
                DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
                channelInfo.setUrl(urlString);

                try {
                    dataBaseHelper.insertItems(channelInfo);
                    final Intent newsIntent = MainActivity.createChannelInfoIntent(channelInfo);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(newsIntent);
                } catch (IOException ex) {
                    return;
                }
            }
        }
    }

    public static Intent createAutoUpdateIntent(final Context context, ArrayList<ChannelInfo> channelInfos) {
        ArrayList<String> URLs = new ArrayList<>();
        for (ChannelInfo channelInfo : channelInfos) {
            URLs.add(channelInfo.getUrl());
        }
        final Intent intent = new Intent(context, NewsUpdateService.class);
        intent.putExtra(URL_ARRAY, URLs);
        return intent;
    }

    private URL getUrl(final String urlString) {
        try {
            return new URL(urlString);
        } catch (MalformedURLException ex) {
            return null;
        }
    }
}