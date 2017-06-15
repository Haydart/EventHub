package pl.rmakowiecki.eventhub.model.remote;

import java.util.List;
import java.util.Map;

public final class RemoteEvent {
    private final String name;
    private final String organizer;
    private final String description;
    private final long timestamp;
    private final String address;
    private final String locationCoordinates;
    private final Map<String, List<String>> eventTags;
    private final Map<String, String> users;

    public RemoteEvent(String name, String description, long timestamp, String organizer,
            String address, String coordinates, Map<String, List<String>> eventTags, Map<String, String> users) {
        this.name = name;
        this.description = description;
        this.timestamp = timestamp;
        this.organizer = organizer;
        this.address = address;
        this.locationCoordinates = coordinates;
        this.eventTags = eventTags;
        this.users = users;
    }

    public Map<String, List<String>> getEventTags() {
        return eventTags;
    }

    public Map<String, String> getUsers() {
        return users;
    }

    public String getAddress() {
        return address;
    }

    public String getOrganizer() {
        return organizer;
    }

    public String getName() {
        return name;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getLocationCoordinates() {
        return locationCoordinates;
    }

    public String getDescription() {
        return description;
    }
}
