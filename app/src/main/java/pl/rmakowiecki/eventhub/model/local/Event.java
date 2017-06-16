package pl.rmakowiecki.eventhub.model.local;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;
import pl.rmakowiecki.eventhub.ui.screen_preference_categories.PreferenceCategory;

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
    private final String description;
    private final String organizer;
    private final long timestamp;
    private final String address;
    private final String locationCoordinates;
    private final List<PreferenceCategory> eventTags;
    private final List<EventAttendee> attendees;

    public Event(String id, String name, String description, long timestamp, String organizer, String address,
            String coordinates, List<PreferenceCategory> eventTags, List<EventAttendee> attendees) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.timestamp = timestamp;
        this.organizer = organizer;
        this.address = address;
        this.locationCoordinates = coordinates;
        this.eventTags = eventTags;
        this.attendees = attendees;
    }

    public Event(Event event) {
        this.id = event.id;
        this.name = event.name;
        this.timestamp = event.timestamp;
        this.description = event.description;
        this.organizer = event.organizer;
        this.address = event.address;
        this.locationCoordinates = event.locationCoordinates;
        this.eventTags = event.eventTags;
        this.attendees = event.attendees;
    }

    public Event() {
        id = "";
        name = "";
        organizer = "";
        timestamp = 0;
        address = "";
        locationCoordinates = "";
        description = "";
        this.eventTags = new ArrayList<>();
        this.attendees = new ArrayList<>();
    }

    protected Event(Parcel in) {
        id = in.readString();
        name = in.readString();
        description = in.readString();
        timestamp = in.readLong();
        organizer = in.readString();
        address = in.readString();
        locationCoordinates = in.readString();
        eventTags = new ArrayList<>();
        in.readTypedList(eventTags, PreferenceCategory.CREATOR);
        attendees = new ArrayList<>();
        in.readTypedList(attendees, EventAttendee.CREATOR);
    }

    public List<PreferenceCategory> getEventTags() {
        return eventTags;
    }

    public List<EventAttendee> getAttendees() {
        return attendees;
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

    public String getDescription() {
        return description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeLong(timestamp);
        dest.writeString(organizer);
        dest.writeString(address);
        dest.writeString(locationCoordinates);
        dest.writeTypedList(eventTags);
        dest.writeTypedList(attendees);
    }
}
