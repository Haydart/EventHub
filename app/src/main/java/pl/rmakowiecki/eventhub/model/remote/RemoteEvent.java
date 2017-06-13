package pl.rmakowiecki.eventhub.model.remote;

import android.os.Parcel;

import java.util.HashMap;
import java.util.Map;

public final class RemoteEvent {
    private final String name;
    private final String organizer;
    private final String description;
    private final long timestamp;
    private final String address;
    private final String locationCoordinates;
    private Map<String, Boolean> users = new HashMap<>();

    public RemoteEvent(String name, String description, long timestamp, String organizer, String address, String coordinates, Map<String, Boolean> users) {
        this.name = name;
        this.description = description;
        this.timestamp = timestamp;
        this.organizer = organizer;
        this.address = address;
        this.locationCoordinates = coordinates;
        this.users = users;
    }

    public RemoteEvent() {
        this.name = "";
        this.description = "";
        this.timestamp = 0;
        this.organizer = "";
        this.address = "";
        this.locationCoordinates = "";
        this.users = new HashMap<>();
    }

    protected RemoteEvent(Parcel in) {
        name = in.readString();
        description = in.readString();
        timestamp = in.readLong();
        organizer = in.readString();
        address = in.readString();
        locationCoordinates = in.readString();
    }

    public Map<String, Boolean> getUsers() {
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
