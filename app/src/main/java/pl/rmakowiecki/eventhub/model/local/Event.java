package pl.rmakowiecki.eventhub.model.local;

import android.os.Parcel;
import android.os.Parcelable;

public final class Event implements Parcelable {
    private final String id;
    private final String name;
    private final String organizer;
    private final long timestamp;
    private final String location;

    public Event(String id, String name, long timestamp, String organizer, String location) {
        this.id = id;
        this.name = name;
        this.timestamp = timestamp;
        this.organizer = organizer;
        this.location = location;
    }

    public Event()
    {
        id = "";
        name = "";
        organizer = "";
        timestamp = 0;
        location = "";
    }

    protected Event(Parcel in) {
        id = in.readString();
        name = in.readString();
        timestamp = in.readLong();
        organizer = in.readString();
        location = in.readString();
    }

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

    public String getLocation() { return location; }

    public String getOrganizer(){ return organizer; }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getTimestamp() {
        return timestamp;
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
