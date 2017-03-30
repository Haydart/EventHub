package pl.rmakowiecki.eventhub.repository;

import pl.rmakowiecki.eventhub.model.local.LocationCoordinates;

public final class LocationSpecification implements Specification {
    private final LocationCoordinates location;

    public LocationSpecification(LocationCoordinates location) {
        this.location = location;
    }

    public LocationCoordinates getLocation() {
        return location;
    }
}
