package pl.rmakowiecki.eventhub.model.local;

import android.os.Parcel;
import android.os.Parcelable;

public final class LocationCoordinates implements Parcelable {
    private final double latitude;
    private final double longitude;

    public LocationCoordinates(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    protected LocationCoordinates(Parcel in) {
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    public static final Creator<LocationCoordinates> CREATOR = new Creator<LocationCoordinates>() {
        @Override
        public LocationCoordinates createFromParcel(Parcel in) {
            return new LocationCoordinates(in);
        }

        @Override
        public LocationCoordinates[] newArray(int size) {
            return new LocationCoordinates[size];
        }
    };

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public String toString() {
        return String.valueOf(latitude) + ", " + String.valueOf(longitude);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }
}
