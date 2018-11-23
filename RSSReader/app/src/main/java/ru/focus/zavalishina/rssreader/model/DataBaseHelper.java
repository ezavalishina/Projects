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

public final class DataBaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "RSSReader.db";
    private static final int SCHEMA = 1;

    private static final String CHANNEL_TABLE_NAME = "channels";
    private static final String COLUMN_CHANNEL_ID = "channel_id";
    private static final String COLUMN_CHANNEL_TITLE = "channel_title";
    private static final String COLUMN_CHANNEL_LINK = "channel_link";
    private static final String COLUMN_CHANNEL_DESCRIPTION = "channel_description";
    private static final String COLUMN_CHANNEL_LAST_BUILD_DATE = "channel_last_build_date";
    private static final String COLUMN_CHANNEL_LANGUAGE = "channel_language";
    private static final String COLUMN_CHANNEL_URL = "channel_url";

    private static final String ITEM_TABLE_NAME = "items";
    private static final String COLUMN_ITEM_ID = "item_id";
    private static final String COLUMN_ITEM_CHANNEL_ID = "item_channel_id";
    private static final String COLUMN_ITEM_TITLE = "item_title";
    private static final String COLUMN_ITEM_LINK = "item_link";
    private static final String COLUMN_ITEM_DESCRIPTION = "item_description";
    private static final String COLUMN_ITEM_PUB_DATE = "item_pub_date";
    private static final String COLUMN_ITEM_AUTHOR = "author";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + CHANNEL_TABLE_NAME + "(" +
                COLUMN_CHANNEL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CHANNEL_TITLE + " TEXT, " +
                COLUMN_CHANNEL_LINK + " TEXT, " +
                COLUMN_CHANNEL_URL + " TEXT, " +
                COLUMN_CHANNEL_DESCRIPTION + " TEXT, " +
                COLUMN_CHANNEL_LAST_BUILD_DATE + " TEXT, " +
                COLUMN_CHANNEL_LANGUAGE + " TEXT" +
                ")");

        db.execSQL("CREATE TABLE " + ITEM_TABLE_NAME + "(" +
                COLUMN_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ITEM_TITLE + " TEXT, " +
                COLUMN_ITEM_LINK + " TEXT, " +
                COLUMN_ITEM_DESCRIPTION + " TEXT, " +
                COLUMN_ITEM_PUB_DATE + " TEXT, " +
                COLUMN_ITEM_AUTHOR + " TEXT, " +
                COLUMN_ITEM_CHANNEL_ID + " INTEGER, " +
                "FOREIGN KEY (" + COLUMN_ITEM_CHANNEL_ID + ") REFERENCES " + CHANNEL_TABLE_NAME + "(" + COLUMN_CHANNEL_ID + ")" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CHANNEL_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ITEM_TABLE_NAME);
        onCreate(db);
    }

    public void insertItems(ChannelInfo channelInfo) throws IOException {
        try {
            ArrayList<ItemInfo> items = channelInfo.getItems();
            ArrayList<ItemInfo> oldItems = getItemInfos(channelInfo);
            ArrayList<ItemInfo> forInsert = new ArrayList<>();
            DateUtil dateUtil = new DateUtil();
            boolean forInsertIsComplete = false;

            final int channelID = getChannelID(channelInfo);

            if (oldItems != null) {

                for (int i = 0; i < items.size(); i++) {
                    if (forInsertIsComplete) {
                        break;
                    }
                    Date insertItemDate = dateUtil.parseDateString(items.get(i).getPubDate());
                    for (int j = 0; j < oldItems.size(); ) {
                        Date dbItemDate = dateUtil.parseDateString(oldItems.get(j).getPubDate());
                        if (dbItemDate != null && insertItemDate != null) {
                            if (insertItemDate.after(dbItemDate)) {
                                forInsert.add(items.get(i));
                                break;
                            } else {
                                forInsertIsComplete = true;
                                break;
                            }
                        } else {
                            forInsertIsComplete = true;
                            break;
                        }

                    }
                }
            } else {
                forInsert = items;
                //Collections.reverse(forInsert);
            }

            SQLiteDatabase database = getWritableDatabase();
            for (int i = 0; i < forInsert.size(); i++) {
                database.insert(ITEM_TABLE_NAME, null,
                        getItemContentValues(forInsert.get(i), channelID));
            }
            database.close();

        } catch (SQLException ex) {
            throw new IOException();
        }
    }

    public void deleteChannel(ChannelInfo channelInfo) {
        SQLiteDatabase database = getWritableDatabase();
        final int channelID = getChannelID(channelInfo);
        database.delete(CHANNEL_TABLE_NAME, COLUMN_CHANNEL_ID + "=" + channelID, null);
        database.delete(ITEM_TABLE_NAME, COLUMN_ITEM_CHANNEL_ID + "=" + channelID, null);
        database.close();
    }

    public ArrayList<ChannelInfo> getChannelInfos() {
        String[] columns = new String[]{COLUMN_CHANNEL_TITLE, COLUMN_CHANNEL_URL, COLUMN_CHANNEL_LINK, COLUMN_CHANNEL_DESCRIPTION,
                COLUMN_CHANNEL_LAST_BUILD_DATE, COLUMN_CHANNEL_LANGUAGE};
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(CHANNEL_TABLE_NAME, columns, null, null,
                null, null, null);

        if (cursor.getCount() == 0) {
            return null;
        }

        ArrayList<ChannelInfo> channelList = new ArrayList<>();

        cursor.moveToNext();
        do {
            ChannelInfo channelInfo = new ChannelInfo();
            channelInfo.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_CHANNEL_TITLE)));
            channelInfo.setLink(cursor.getString(cursor.getColumnIndex(COLUMN_CHANNEL_LINK)));
            channelInfo.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_CHANNEL_DESCRIPTION)));
            channelInfo.setLastBuildDate(cursor.getString(cursor.getColumnIndex(COLUMN_CHANNEL_LAST_BUILD_DATE)));
            channelInfo.setLanguage(cursor.getString(cursor.getColumnIndex(COLUMN_CHANNEL_LANGUAGE)));
            channelInfo.setUrl(cursor.getString(cursor.getColumnIndex(COLUMN_CHANNEL_URL)));
            channelInfo.setItems(getItemInfos(channelInfo));

            channelList.add(channelInfo);
        } while (cursor.moveToNext());

        cursor.close();
        database.close();

        return channelList;
    }

    public ArrayList<ItemInfo> getItemInfos(ChannelInfo channelInfo) {
        if (channelInfo == null) {
            return null;
        }
        String[] columns = new String[]{COLUMN_ITEM_CHANNEL_ID, COLUMN_ITEM_AUTHOR, COLUMN_ITEM_ID,
                COLUMN_ITEM_PUB_DATE, COLUMN_ITEM_DESCRIPTION, COLUMN_ITEM_LINK, COLUMN_ITEM_TITLE};
        final int channelID = getChannelID(channelInfo);
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(ITEM_TABLE_NAME, columns, COLUMN_ITEM_CHANNEL_ID + " = ?",
                new String[]{String.valueOf(channelID)}, null, null, COLUMN_ITEM_ID);

        if (cursor.getCount() == 0) {
            return null;
        }

        ArrayList<ItemInfo> itemList = new ArrayList<>();

        cursor.moveToFirst();

        do {
            ItemInfo itemInfo = new ItemInfo();
            itemInfo.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_TITLE)));
            itemInfo.setLink(cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_LINK)));
            itemInfo.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_DESCRIPTION)));
            itemInfo.setPubDate(cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_PUB_DATE)));
            itemInfo.setAuthor(cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_AUTHOR)));

            itemList.add(itemInfo);
        } while (cursor.moveToNext());

        database.close();
        cursor.close();

        return itemList;
    }

    private int getChannelID(ChannelInfo channelInfo) {
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(CHANNEL_TABLE_NAME, new String[]{COLUMN_CHANNEL_ID},
                COLUMN_CHANNEL_URL + " = ?", new String[]{channelInfo.getUrl()},
                null, null, null);
        final int channelID;

        if (cursor.getCount() != 0) {
            cursor.moveToNext();
            channelID = cursor.getInt(cursor.getColumnIndex(COLUMN_CHANNEL_ID));
        } else {
            channelID = (int) database.insert(CHANNEL_TABLE_NAME, null,
                    getChannelContentValues(channelInfo));
        }

        cursor.close();

        return channelID;
    }

    public boolean inDataBase(ChannelInfo channelInfo) {
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(CHANNEL_TABLE_NAME, new String[]{COLUMN_CHANNEL_ID},
                COLUMN_CHANNEL_URL + " = ?", new String[]{channelInfo.getUrl()},
                null, null, null);

        if (cursor.getCount() != 0) {
            return true;
        }

        return false;
    }

    private ContentValues getChannelContentValues(ChannelInfo channelInfo) {
        ContentValues channelContentValues = new ContentValues();
        channelContentValues.put(COLUMN_CHANNEL_TITLE, channelInfo.getTitle());
        channelContentValues.put(COLUMN_CHANNEL_LINK, channelInfo.getLink());
        channelContentValues.put(COLUMN_CHANNEL_DESCRIPTION, channelInfo.getDescription());
        channelContentValues.put(COLUMN_CHANNEL_LAST_BUILD_DATE, channelInfo.getLastBuildDate());
        channelContentValues.put(COLUMN_CHANNEL_LANGUAGE, channelInfo.getLanguage());
        channelContentValues.put(COLUMN_CHANNEL_URL, channelInfo.getUrl());

        return channelContentValues;
    }

    private ContentValues getItemContentValues(ItemInfo itemInfo, int channelID) {
        ContentValues itemContentValues = new ContentValues();
        itemContentValues.put(COLUMN_ITEM_CHANNEL_ID, channelID);
        itemContentValues.put(COLUMN_ITEM_TITLE, itemInfo.getTitle());
        itemContentValues.put(COLUMN_ITEM_LINK, itemInfo.getLink());
        itemContentValues.put(COLUMN_ITEM_DESCRIPTION, itemInfo.getDescription());
        itemContentValues.put(COLUMN_ITEM_PUB_DATE, itemInfo.getPubDate());
        itemContentValues.put(COLUMN_ITEM_AUTHOR, itemInfo.getAuthor());

        return itemContentValues;
    }
}