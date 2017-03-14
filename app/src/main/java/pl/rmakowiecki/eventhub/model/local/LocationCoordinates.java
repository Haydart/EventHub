package pl.rmakowiecki.eventhub.model.local;

public final class LocationCoordinates {
    private final double latitude;
    private final double longitude;

    public LocationCoordinates(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

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
}
