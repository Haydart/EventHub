package pl.rmakowiecki.eventhub.model.local;

import pl.rmakowiecki.eventhub.repository.DataItem;

public final class Event implements DataItem {
    private final String id;
    private final String name;
    private final long timestamp;

    public Event(String id, String name, long timestamp) {
        this.id = id;
        this.name = name;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
