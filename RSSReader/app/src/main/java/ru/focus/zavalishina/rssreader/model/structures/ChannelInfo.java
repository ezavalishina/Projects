package ru.focus.zavalishina.rssreader.model.structures;

import java.util.ArrayList;

public final class ChannelInfo implements java.io.Serializable {
    private String title = "";
    private String link = "";
    private String description = "";
    private String lastBuildDate = "";
    private String language = "";
    private String url = "";
    private ArrayList<ItemInfo> items = new ArrayList<>();

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public String getLastBuildDate() {
        return lastBuildDate;
    }

    public String getLanguage() {
        return language;
    }

    public String getUrl() {
        return url;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public void setLink(final String link) {
        this.link = link;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public void setLastBuildDate(final String lastBuildDate) {
        this.lastBuildDate = lastBuildDate;
    }

    public void setLanguage(final String language) {
        this.language = language;
    }

    public void addItem(final ItemInfo item) {
        items.add(item);
    }

    public ArrayList<ItemInfo> getItems() {
        return items;
    }

    public void setItems(ArrayList<ItemInfo> items) {
        this.items = items;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
