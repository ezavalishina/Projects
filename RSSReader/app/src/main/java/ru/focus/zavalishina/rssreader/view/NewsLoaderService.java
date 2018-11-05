package ru.focus.zavalishina.rssreader.view;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import java.net.MalformedURLException;
import java.net.URL;

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
        final Intent newsIntent = MainActivity.createChannelInfoIntent(channelInfo);
        sendBroadcast(newsIntent);
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
