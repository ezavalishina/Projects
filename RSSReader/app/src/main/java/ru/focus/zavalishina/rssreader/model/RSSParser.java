package ru.focus.zavalishina.rssreader.model;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public final class RSSParser {
    public ChannelInfo parse(final XmlPullParser xmlPullParser) {
        final ChannelInfo channel = new ChannelInfo();
        String tagName = null;
        int currentEvent;

        try {
            currentEvent = xmlPullParser.getEventType();
            while (XmlPullParser.END_DOCUMENT != currentEvent) {

                if (XmlPullParser.START_TAG == currentEvent) {
                    tagName = xmlPullParser.getName();
                }

                if (XmlPullParser.TEXT == currentEvent && null != tagName) {
                    switch (tagName) {
                        case "title":
                            channel.setTitle(xmlPullParser.getText());
                            break;
                        case "link":
                            channel.setLink(xmlPullParser.getText());
                            break;
                        case "description":
                            channel.setDescription(xmlPullParser.getText());
                            break;
                        case "lastBuildDate":
                            channel.setLastBuildDate(xmlPullParser.getText());
                            break;
                        case "language":
                            channel.setLanguage(xmlPullParser.getText());
                            break;
                        case "item":
                            ItemInfo item = parseItem(xmlPullParser);
                            if (item != null) {
                                channel.addItem(item);
                            }
                            tagName = null;
                            break;

                        default:
                            break;
                    }
                }

                if (XmlPullParser.END_TAG == currentEvent) {
                    tagName = null;
                }

                currentEvent = xmlPullParser.next();
            }
        } catch (XmlPullParserException | IOException ex) {
            return null;
        }
        return channel;
    }

    private ItemInfo parseItem(final XmlPullParser xmlPullParser) {
        final ItemInfo item = new ItemInfo();
        String tagName = null;
        int currentEvent;

        try {
            currentEvent = xmlPullParser.getEventType();
            while (!("item".equals(xmlPullParser.getName()) && XmlPullParser.END_TAG == currentEvent)) {

                if (XmlPullParser.START_TAG == currentEvent) {
                    tagName = xmlPullParser.getName();
                }

                if (XmlPullParser.TEXT == currentEvent && null != tagName) {
                    switch (tagName) {
                        case "title":
                            item.setTitle(xmlPullParser.getText());
                            break;
                        case "link":
                            item.setLink(xmlPullParser.getText());
                            break;
                        case "description":
                            item.setDescription(xmlPullParser.getText());
                            break;
                        case "pubDate":
                            item.setPubDate(xmlPullParser.getText());
                            break;
                        case "author":
                            item.setAuthor(xmlPullParser.getText());
                            break;

                        default:
                            break;

                    }
                }

                if (XmlPullParser.END_TAG == currentEvent) {
                    tagName = null;
                }

                currentEvent = xmlPullParser.next();
            }
        } catch (XmlPullParserException | IOException ex) {
            return null;
        }
        return item;
    }
}
