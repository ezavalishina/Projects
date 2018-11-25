package ru.focus.zavalishina.rssreader.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import ru.focus.zavalishina.rssreader.model.structures.ChannelInfo;
import ru.focus.zavalishina.rssreader.model.DataBaseHelper;
import ru.focus.zavalishina.rssreader.model.structures.ItemInfo;
import ru.focus.zavalishina.rssreader.model.NewsLoader;
import ru.focus.zavalishina.rssreader.view.activities.MainActivity;
import ru.focus.zavalishina.rssreader.view.activities.NewsListActivity;

public final class NewsLoadService extends IntentService {

    public NewsLoadService() {
        super("NewsLoadService");
    }

    @Override
    public void onHandleIntent(final Intent intent) {
        if (intent == null) {
            return;
        }
        final String urlString = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (urlString == null) {
            return;
        }
        final URL url = getUrl(urlString);
        if (url == null) {
            return;
        }
        final NewsLoader newsLoader = new NewsLoader();
        final ChannelInfo channelInfo = newsLoader.loadWithUrl(url);

        if (channelInfo != null) {
            DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
            channelInfo.setUrl(urlString);

            boolean inDataBase = dataBaseHelper.inDataBase(channelInfo);
            try {
                dataBaseHelper.insertItems(channelInfo);
            } catch (IOException ex) {
                return;
            }

            if (!inDataBase) {
                final Intent newsIntent = MainActivity.createChannelInfoIntent(channelInfo);
                LocalBroadcastManager.getInstance(this).sendBroadcast(newsIntent);
            } else {
                ArrayList<ItemInfo> itemInfos = dataBaseHelper.getItemInfos(channelInfo);
                channelInfo.setItems(itemInfos);
                final Intent newsIntent = NewsListActivity.createUpdateChannelInfoIntent(channelInfo);
                LocalBroadcastManager.getInstance(this).sendBroadcast(newsIntent);
            }
        }
    }



    public static Intent createIntent(final Context context, final String url) {
        final Intent intent = new Intent(context, NewsLoadService.class);
        intent.putExtra(Intent.EXTRA_TEXT, url);
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