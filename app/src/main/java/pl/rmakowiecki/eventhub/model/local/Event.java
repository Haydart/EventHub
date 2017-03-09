package pl.rmakowiecki.eventhub.model.local;

import pl.rmakowiecki.eventhub.repository.DataItem;

public class Event implements DataItem {
    String id;
    String name;
    long timestamp;
}
