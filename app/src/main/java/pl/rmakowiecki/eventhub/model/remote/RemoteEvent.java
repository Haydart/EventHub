package pl.rmakowiecki.eventhub.model.remote;

import android.os.Parcel;
import java.util.HashMap;

public final class RemoteEvent {
    private final String name;
    private final String organizer;
    private final long timestamp;
    private final String address;
    private final String locationCoordinates;
    private HashMap<String, Boolean> users = new HashMap<>();

    public RemoteEvent(String name, long timestamp, String organizer, String address, String coordinates, HashMap<String, Boolean> users) {
        this.name = name;
        this.timestamp = timestamp;
        this.organizer = organizer;
        this.address = address;
        this.locationCoordinates = coordinates;
        this.users = users;
    }

    protected RemoteEvent(Parcel in) {
        name = in.readString();
        timestamp = in.readLong();
        organizer = in.readString();
        address = in.readString();
        locationCoordinates = in.readString();
    }

    public HashMap<String, Boolean> getUsers() {
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
}
