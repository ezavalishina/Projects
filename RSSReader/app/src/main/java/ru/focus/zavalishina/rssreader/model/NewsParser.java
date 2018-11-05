package ru.focus.zavalishina.rssreader.model;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.Reader;

final class NewsParser {
    final ChannelInfo parse(final Reader reader) {
        try {
            final XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            final XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(reader);
            return new RSSParser().parse(xmlPullParser);
        } catch (XmlPullParserException ex) {
            return null;
        }
    }
}
