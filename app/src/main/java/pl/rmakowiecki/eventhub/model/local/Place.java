package pl.rmakowiecki.eventhub.model.local;

public final class Place {
    private final String id;
    private final String name;
    private final String address;
    private final LocationCoordinates locationCoordinates;

    public Place(String id, String name, String address, LocationCoordinates locationCoordinates) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.locationCoordinates = locationCoordinates;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public LocationCoordinates getLocationCoordinates() {
        return locationCoordinates;
    }
}
