package pl.rmakowiecki.eventhub.model.local;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.HashMap;

public final class Event implements Parcelable {
    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };
    private final String id;
    private final String name;
    private final String organizer;
    private final long timestamp;
    private final String address;
    private final String locationCoordinates;
    private HashMap<String, Boolean> users = new HashMap<>();

    public Event(String id, String name, long timestamp, String organizer, String address, String coordinates, HashMap<String, Boolean> users) {
        this.id = id;
        this.name = name;
        this.timestamp = timestamp;
        this.organizer = organizer;
        this.address = address;
        this.locationCoordinates = coordinates;
        this.users = users;
    }

    public Event() {
        id = "";
        name = "";
        organizer = "";
        timestamp = 0;
        address = "";
        locationCoordinates = "";
    }

    protected Event(Parcel in) {
        id = in.readString();
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

    public String getId() {
        return id;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeLong(timestamp);
    }
}
