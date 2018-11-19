package ru.focus.zavalishina.rssreader.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static final SimpleDateFormat rfc822DateFormats[] = new SimpleDateFormat[] {
            new SimpleDateFormat("EEE, d MMM yy HH:mm:ss z"),
            new SimpleDateFormat("EEE, d MMM yy HH:mm z"),
            new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z"),
            new SimpleDateFormat("EEE, d MMM yyyy HH:mm z"),
            new SimpleDateFormat("d MMM yy HH:mm z"),
            new SimpleDateFormat("d MMM yy HH:mm:ss z"),
            new SimpleDateFormat("d MMM yyyy HH:mm z"),
            new SimpleDateFormat("d MMM yyyy HH:mm:ss z")
    };

    public static Date parseRfc822DateString(String dateString) {
        Date date = null;
        for (SimpleDateFormat sdf : rfc822DateFormats) {
            try {
                date = sdf.parse(dateString);
            } catch (ParseException e) {

            }
            if (date != null) {
                return date;
            }
        }
        return null;
    }

}