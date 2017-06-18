package pl.rmakowiecki.eventhub.util;

import pl.rmakowiecki.eventhub.api.EventImageStorageInteractor;
import rx.Observable;

public class EventUtils {
    public static Observable<byte[]> getEventPicture(String eventId) {
        return new EventImageStorageInteractor(eventId).getData();
    }
}
