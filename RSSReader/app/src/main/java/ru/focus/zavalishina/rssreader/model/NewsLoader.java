package ru.focus.zavalishina.rssreader.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;

import ru.focus.zavalishina.rssreader.model.parser.NewsParser;
import ru.focus.zavalishina.rssreader.model.structures.ChannelInfo;

public final class NewsLoader {

    public ChannelInfo loadWithUrl(final URL url) {
        final ChannelInfo channelInfo;
        final URLConnection urlConnection;
        InputStream inputStream = null;
        Reader reader = null;
        final NewsParser newsParser;
        try {
            urlConnection = url.openConnection();
            inputStream = urlConnection.getInputStream();
            reader = new InputStreamReader(inputStream);
            newsParser = new NewsParser();
            channelInfo = newsParser.parse(reader);
            return channelInfo;
        } catch (IOException ex) {
            return null;
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException ex) {
                //TODO логи
            }
        }
    }
}