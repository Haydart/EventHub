
package pl.rmakowiecki.eventhub.model.remote.google;

import com.google.gson.annotations.SerializedName;

public class Coordinates {
    @SerializedName("lat") public final double latitude;
    @SerializedName("lng") public final double longitude;

    public Coordinates(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
