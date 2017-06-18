package pl.rmakowiecki.eventhub.ui.screen_event_details;

import pl.rmakowiecki.eventhub.repository.Specification;

public class EventImageSpecification implements Specification {

    private String firebaseKey;

    public EventImageSpecification(String key) {
        firebaseKey = key;
    }

    public String getFirebaseKey() {
        return firebaseKey;
    }
}
