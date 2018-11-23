package ru.focus.zavalishina.rssreader.model;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.Reader;

final class NewsParser {
    final ChannelInfo parse(final Reader reader) {
        try {
            final XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            final XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(reader);

            while (!(xmlPullParser.getEventType() == XmlPullParser.END_DOCUMENT)) {
                if (xmlPullParser.getEventType() == XmlPullParser.START_TAG) {
                    if ("rss".equals(xmlPullParser.getName()))
                    {
                        return new RSSParser().parse(xmlPullParser);
                    }

                    if ("feed".equals(xmlPullParser.getName()))
                    {
                        return new AtomParser().parse(xmlPullParser);
                    }
                }
                xmlPullParser.next();
            }
        } catch (XmlPullParserException ex) {
            return null;
        } catch (IOException ex) {
            return null;
        }
        return null;
    }
}
