package ru.focus.zavalishina.rssreader.view;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.support.v4.content.LocalBroadcastManager;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import ru.focus.zavalishina.rssreader.model.DataBaseHelper;
import ru.focus.zavalishina.rssreader.model.ItemInfo;
import ru.focus.zavalishina.rssreader.view.activities.MainActivity;
import ru.focus.zavalishina.rssreader.model.ChannelInfo;
import ru.focus.zavalishina.rssreader.model.NewsLoader;
import ru.focus.zavalishina.rssreader.view.activities.NewsListActivity;

public final class NewsLoaderService extends IntentService {

    public NewsLoaderService() {
        super("NewsLoaderService");
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        if (intent == null) {
            return;
        }
        final String urlString = intent.getStringExtra(Intent.EXTRA_TEXT);
        final URL url = getUrl(urlString);
        if (url == null) {
            return;
        }
        final NewsLoader newsLoader = new NewsLoader();
        final ChannelInfo channelInfo = newsLoader.loadWithUrl(url);
        channelInfo.setUrl(urlString);
        DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
        if (channelInfo != null) {
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
        final Intent intent = new Intent(context, NewsLoaderService.class);
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