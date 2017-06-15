package pl.rmakowiecki.eventhub.model.local;

import android.os.Parcel;
import android.os.Parcelable;

public final class EventAttendee implements Parcelable {

    public static final Creator<EventAttendee> CREATOR = new Creator<EventAttendee>() {
        @Override
        public EventAttendee createFromParcel(Parcel in) {
            return new EventAttendee(in);
        }

        @Override
        public EventAttendee[] newArray(int size) {
            return new EventAttendee[size];
        }
    };

    private final String id;
    private final String name;

    public EventAttendee(String id, String name) {
        this.id = id;
        this.name = name;
    }

    protected EventAttendee(Parcel in) {
        id = in.readString();
        name = in.readString();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
    }
}
