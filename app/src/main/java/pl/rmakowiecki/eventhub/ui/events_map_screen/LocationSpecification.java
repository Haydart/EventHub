package pl.rmakowiecki.eventhub.ui.events_map_screen;

import pl.rmakowiecki.eventhub.model.local.LocationCoordinates;
import pl.rmakowiecki.eventhub.repository.Specification;

public final class LocationSpecification implements Specification {
    private final LocationCoordinates location;

    public LocationSpecification(LocationCoordinates location) {
        this.location = location;
    }

    public LocationCoordinates getLocation() {
        return location;
    }
}
