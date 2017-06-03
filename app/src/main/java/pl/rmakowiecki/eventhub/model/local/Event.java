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
    private final String location;
    private final String coordinates;
    private HashMap<String, Boolean> users = new HashMap<>();

    public Event(String id, String name, long timestamp, String organizer, String location, String coordinates, HashMap<String, Boolean> users) {
        this.id = id;
        this.name = name;
        this.timestamp = timestamp;
        this.organizer = organizer;
        this.location = location;
        this.coordinates = coordinates;
        this.users = users;
    }

    public Event() {
        id = "";
        name = "";
        organizer = "";
        timestamp = 0;
        location = "";
        coordinates = "";
    }

    protected Event(Parcel in) {
        id = in.readString();
        name = in.readString();
        timestamp = in.readLong();
        organizer = in.readString();
        location = in.readString();
        coordinates = in.readString();
    }

    public HashMap<String, Boolean> getUsers() {
        return users;
    }

    public String getLocation() {
        return location;
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

    public String getCoordinates() {
        return coordinates;
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
