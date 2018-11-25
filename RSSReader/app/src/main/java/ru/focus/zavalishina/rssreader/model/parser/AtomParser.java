package ru.focus.zavalishina.rssreader.model.parser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import ru.focus.zavalishina.rssreader.model.structures.ChannelInfo;
import ru.focus.zavalishina.rssreader.model.structures.ItemInfo;

final class AtomParser {
    ChannelInfo parse(final XmlPullParser xmlPullParser) {
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
                            channel.setTitle(xmlPullParser.getText());
                            break;
                        case "updated":
                            channel.setLastBuildDate(xmlPullParser.getText());
                            break;
                        case "entry":
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
        ArrayList<ItemInfo> itemInfos = channel.getItems();
        Collections.reverse(itemInfos);
        channel.setItems(itemInfos);
        return channel;
    }

    private ItemInfo parseItem(final XmlPullParser xmlPullParser) {
        final ItemInfo item = new ItemInfo();
        String tagName = null;
        int currentEvent;

        try {
            currentEvent = xmlPullParser.getEventType();
            while (!("entry".equals(xmlPullParser.getName()) && XmlPullParser.END_TAG == currentEvent)) {

                if (XmlPullParser.START_TAG == currentEvent) {
                    tagName = xmlPullParser.getName();
                }

                if (XmlPullParser.TEXT == currentEvent && null != tagName) {
                    switch (tagName) {
                        case "title":
                            item.setTitle(xmlPullParser.getText());
                            break;
                        case "summary":
                            item.setDescription(xmlPullParser.getText());
                            break;
                        case "updated":
                            item.setPubDate(xmlPullParser.getText());
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
