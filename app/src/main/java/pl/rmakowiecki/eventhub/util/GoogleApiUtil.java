package pl.rmakowiecki.eventhub.util;

import pl.rmakowiecki.eventhub.model.local.LocationCoordinates;

public class GoogleApiUtil {
    public static String constructLocationString(LocationCoordinates location) {
        return location != null ? (location.getLatitude() + "," + location.getLongitude()) : "";
    }
}
