package ru.focus.zavalishina.rssreader.view;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import ru.focus.zavalishina.rssreader.model.DataBaseHelper;
import ru.focus.zavalishina.rssreader.view.activities.MainActivity;
import ru.focus.zavalishina.rssreader.model.ChannelInfo;
import ru.focus.zavalishina.rssreader.model.NewsLoader;

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
        DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
        if (channelInfo != null) {
            try {
                dataBaseHelper.insertItems(channelInfo);
            } catch (SQLiteConstraintException ignored) {

            } catch (IOException ex) {
                return;
            }


            if (dataBaseHelper.getChannelInfos() != null) {
                if (dataBaseHelper.getChannelInfos().get(0) != null) {
                    ChannelInfo channelInfo1 = dataBaseHelper.getChannelInfos().get(0);
                    final Intent newsIntent = MainActivity.createChannelInfoIntent(channelInfo1);
                    sendBroadcast(newsIntent);
                }
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
            final URL url = new URL(urlString);
            return url;
        } catch (MalformedURLException ex) {
            return null;
        }
    }
}