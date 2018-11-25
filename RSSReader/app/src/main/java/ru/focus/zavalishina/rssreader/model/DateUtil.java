package ru.focus.zavalishina.rssreader.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
    private static final SimpleDateFormat DateFormat[] = new SimpleDateFormat[] {
            new SimpleDateFormat("EEE, dd MMM yy HH:mm:ss Z", Locale.ENGLISH),
            new SimpleDateFormat("EEE, d MMM yy HH:mm Z", Locale.ENGLISH),
            new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.ENGLISH),
            new SimpleDateFormat("EEE, d MMM yyyy HH:mm Z", Locale.ENGLISH),
            new SimpleDateFormat("d MMM yy HH:mm Z", Locale.ENGLISH),
            new SimpleDateFormat("d MMM yy HH:mm:ss Z", Locale.ENGLISH),
            new SimpleDateFormat("d MMM yyyy HH:mm Z", Locale.ENGLISH),
            new SimpleDateFormat("d MMM yyyy HH:mm:ss Z", Locale.ENGLISH),
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.ENGLISH),
    };

    public static Date parseDateString(String dateString) {
        Date date = null;
        for (SimpleDateFormat sdf : DateFormat) {
            try {
                date = sdf.parse(dateString);
            } catch (ParseException ignored) {

            }
            if (date != null) {
                return date;
            }
        }

        return null;
    }

}