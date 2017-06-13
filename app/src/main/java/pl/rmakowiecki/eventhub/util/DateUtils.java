package pl.rmakowiecki.eventhub.util;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateUtils {
    public static String getFormattedDate(DateTime dateTime, String pattern) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern(pattern);
        return dateTime.toString(formatter);
    }
}
