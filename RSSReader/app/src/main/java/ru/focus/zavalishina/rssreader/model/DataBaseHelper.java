package ru.focus.zavalishina.rssreader.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import ru.focus.zavalishina.rssreader.model.structures.ChannelInfo;
import ru.focus.zavalishina.rssreader.model.structures.ItemInfo;

public final class DataBaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "RSSReader.db";
    private static final int SCHEMA = 1;
    private static final int ITEMS_NUMBER_IN_DB = 100;

    private static final class ChannelTable {
        private static final String NAME = "channels";
        private static final String ID = "id";
        private static final String TITLE = "title";
        private static final String LINK = "link";
        private static final String DESCRIPTION = "description";
        private static final String LAST_BUILD_DATE = "last_build_date";
        private static final String LANGUAGE = "language";
        private static final String URL = "url";
    }

    private static final class ItemTable {
        private static final String NAME = "items";
        private static final String ID = "id";
        private static final String CHANNEL_ID = "channel_id";
        private static final String TITLE = "title";
        private static final String LINK = "link";
        private static final String DESCRIPTION = "description";
        private static final String PUB_DATE = "pub_date";
        private static final String AUTHOR = "author";
    }

    public DataBaseHelper(final Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + ChannelTable.NAME + "(" +
                ChannelTable.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ChannelTable.TITLE + " TEXT, " +
                ChannelTable.LINK + " TEXT, " +
                ChannelTable.URL + " TEXT, " +
                ChannelTable.DESCRIPTION + " TEXT, " +
                ChannelTable.LAST_BUILD_DATE + " TEXT, " +
                ChannelTable.LANGUAGE + " TEXT" +
                ")");

        db.execSQL("CREATE TABLE " + ItemTable.NAME + "(" +
                ItemTable.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ItemTable.TITLE + " TEXT, " +
                ItemTable.LINK + " TEXT, " +
                ItemTable.DESCRIPTION + " TEXT, " +
                ItemTable.PUB_DATE + " TEXT, " +
                ItemTable.AUTHOR + " TEXT, " +
                ItemTable.CHANNEL_ID + " INTEGER, " +
                "FOREIGN KEY (" + ItemTable.CHANNEL_ID + ") REFERENCES " + ChannelTable.NAME + "(" + ChannelTable.ID + ")" +
                ")");
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ChannelTable.NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ItemTable.NAME);
        onCreate(db);
    }

    public void insertItems(final ChannelInfo channelInfo) throws IOException {
        try {
            final ArrayList<ItemInfo> items = channelInfo.getItems();
            final ArrayList<ItemInfo> oldItems = getItemInfos(channelInfo);
            ArrayList<ItemInfo> forInsert = new ArrayList<>();

            final int channelID = getChannelID(channelInfo);

            if (oldItems != null) {
                final Date dbItemDate = DateUtil.parseDateString(oldItems.get(oldItems.size() - 1).getPubDate());
                for (int i = items.size() - 1; i >= 0; i--) {
                    final Date insertItemDate = DateUtil.parseDateString(items.get(i).getPubDate());
                    if (dbItemDate != null && insertItemDate != null) {
                        if (insertItemDate.after(dbItemDate)) {
                            forInsert.add(items.get(i));
                        } else {
                            break;
                        }
                    }
                }
            } else {
                forInsert = items;
            }

            final SQLiteDatabase database = getWritableDatabase();
            for (int i = 0; i < forInsert.size(); i++) {
                database.insert(ItemTable.NAME, null,
                        getItemContentValues(forInsert.get(i), channelID));
            }
            trimItems(database, channelID);
            database.close();

        } catch (SQLException ex) {
            throw new IOException();
        }
    }

    public void deleteChannel(final ChannelInfo channelInfo) {
        final SQLiteDatabase database = getWritableDatabase();
        final int channelID = getChannelID(channelInfo);
        database.delete(ChannelTable.NAME, ChannelTable.ID + "=" + channelID, null);
        database.delete(ItemTable.NAME, ItemTable.CHANNEL_ID + "=" + channelID, null);
        database.close();
    }

    private void trimItems(final SQLiteDatabase database, final int channelID) {
        final String[] columns = new String[]{ItemTable.CHANNEL_ID, ItemTable.AUTHOR, ItemTable.ID,
                ItemTable.PUB_DATE, ItemTable.DESCRIPTION, ItemTable.LINK, ItemTable.TITLE};
        final Cursor cursor = database.query(ItemTable.NAME, columns,
                ItemTable.CHANNEL_ID + "=" + String.valueOf(channelID),
                null, null, null, null);

        final int cursorCount = cursor.getCount();
        if (cursorCount == 0) {
            return;
        }
        if (cursorCount > ITEMS_NUMBER_IN_DB) {
            final int excess = cursorCount - ITEMS_NUMBER_IN_DB;
            cursor.moveToFirst();

            for (int i = 0; i < excess; i++) {
                final int id = cursor.getInt(cursor.getColumnIndex(ItemTable.ID));
                database.delete(ItemTable.NAME, ItemTable.ID + "=" + id, null);
                cursor.moveToNext();
            }
        }
        cursor.close();
    }

    public ArrayList<ChannelInfo> getChannelInfos() {
        final String[] columns = new String[]{ChannelTable.TITLE, ChannelTable.URL, ChannelTable.LINK, ChannelTable.DESCRIPTION,
                ChannelTable.LAST_BUILD_DATE, ChannelTable.LANGUAGE};
        final SQLiteDatabase database = getReadableDatabase();
        final Cursor cursor = database.query(ChannelTable.NAME, columns, null, null,
                null, null, null);

        if (cursor.getCount() == 0) {
            return null;
        }

        final ArrayList<ChannelInfo> channelList = new ArrayList<>();

        cursor.moveToNext();
        do {
            ChannelInfo channelInfo = new ChannelInfo();
            channelInfo.setTitle(cursor.getString(cursor.getColumnIndex(ChannelTable.TITLE)));
            channelInfo.setLink(cursor.getString(cursor.getColumnIndex(ChannelTable.LINK)));
            channelInfo.setDescription(cursor.getString(cursor.getColumnIndex(ChannelTable.DESCRIPTION)));
            channelInfo.setLastBuildDate(cursor.getString(cursor.getColumnIndex(ChannelTable.LAST_BUILD_DATE)));
            channelInfo.setLanguage(cursor.getString(cursor.getColumnIndex(ChannelTable.LANGUAGE)));
            channelInfo.setUrl(cursor.getString(cursor.getColumnIndex(ChannelTable.URL)));
            channelInfo.setItems(getItemInfos(channelInfo));

            channelList.add(channelInfo);
        } while (cursor.moveToNext());

        cursor.close();
        database.close();

        return channelList;
    }

    public ArrayList<ItemInfo> getItemInfos(final ChannelInfo channelInfo) {
        if (channelInfo == null) {
            return null;
        }
        final String[] columns = new String[]{ItemTable.CHANNEL_ID, ItemTable.AUTHOR, ItemTable.ID,
                ItemTable.PUB_DATE, ItemTable.DESCRIPTION, ItemTable.LINK, ItemTable.TITLE};
        final int channelID = getChannelID(channelInfo);
        final SQLiteDatabase database = getReadableDatabase();
        final Cursor cursor = database.query(ItemTable.NAME, columns, ItemTable.CHANNEL_ID + " = ?",
                new String[]{String.valueOf(channelID)}, null, null, ItemTable.ID);

        if (cursor.getCount() == 0) {
            return null;
        }

        final ArrayList<ItemInfo> itemList = new ArrayList<>();

        cursor.moveToFirst();

        do {
            ItemInfo itemInfo = new ItemInfo();
            itemInfo.setTitle(cursor.getString(cursor.getColumnIndex(ItemTable.TITLE)));
            itemInfo.setLink(cursor.getString(cursor.getColumnIndex(ItemTable.LINK)));
            itemInfo.setDescription(cursor.getString(cursor.getColumnIndex(ItemTable.DESCRIPTION)));
            itemInfo.setPubDate(cursor.getString(cursor.getColumnIndex(ItemTable.PUB_DATE)));
            itemInfo.setAuthor(cursor.getString(cursor.getColumnIndex(ItemTable.AUTHOR)));

            itemList.add(itemInfo);
        } while (cursor.moveToNext());

        database.close();
        cursor.close();

        return itemList;
    }

    private int getChannelID(final ChannelInfo channelInfo) {
        final SQLiteDatabase database = getReadableDatabase();
        final Cursor cursor = database.query(ChannelTable.NAME, new String[]{ChannelTable.ID},
                ChannelTable.URL + " = ?", new String[]{channelInfo.getUrl()},
                null, null, null);
        final int channelID;

        if (cursor.getCount() != 0) {
            cursor.moveToNext();
            channelID = cursor.getInt(cursor.getColumnIndex(ChannelTable.ID));
        } else {
            channelID = (int) database.insert(ChannelTable.NAME, null,
                    getChannelContentValues(channelInfo));
        }

        cursor.close();

        return channelID;
    }

    public boolean inDataBase(final ChannelInfo channelInfo) {
        final SQLiteDatabase database = getReadableDatabase();
        final Cursor cursor = database.query(ChannelTable.NAME, new String[]{ChannelTable.ID},
                ChannelTable.URL + " = ?", new String[]{channelInfo.getUrl()},
                null, null, null);

        if (cursor.getCount() != 0) {
            cursor.close();
            return true;
        }

        cursor.close();

        return false;
    }

    private ContentValues getChannelContentValues(final ChannelInfo channelInfo) {
        final ContentValues channelContentValues = new ContentValues();
        channelContentValues.put(ChannelTable.TITLE, channelInfo.getTitle());
        channelContentValues.put(ChannelTable.LINK, channelInfo.getLink());
        channelContentValues.put(ChannelTable.DESCRIPTION, channelInfo.getDescription());
        channelContentValues.put(ChannelTable.LAST_BUILD_DATE, channelInfo.getLastBuildDate());
        channelContentValues.put(ChannelTable.LANGUAGE, channelInfo.getLanguage());
        channelContentValues.put(ChannelTable.URL, channelInfo.getUrl());

        return channelContentValues;
    }

    private ContentValues getItemContentValues(final ItemInfo itemInfo, final int channelID) {
        final ContentValues itemContentValues = new ContentValues();
        itemContentValues.put(ItemTable.CHANNEL_ID, channelID);
        itemContentValues.put(ItemTable.TITLE, itemInfo.getTitle());
        itemContentValues.put(ItemTable.LINK, itemInfo.getLink());
        itemContentValues.put(ItemTable.DESCRIPTION, itemInfo.getDescription());
        itemContentValues.put(ItemTable.PUB_DATE, itemInfo.getPubDate());
        itemContentValues.put(ItemTable.AUTHOR, itemInfo.getAuthor());

        return itemContentValues;
    }
}