package pl.rmakowiecki.eventhub;

import pl.rmakowiecki.eventhub.model.local.LocationCoordinates;
import pl.rmakowiecki.eventhub.ui.events_map_screen.StatusWrapper;
import rx.Observable;

public interface LocationProvider {
    Observable<StatusWrapper> isLocationTurnedOn();

    Observable<LocationCoordinates> getLocation();

    Observable<LocationCoordinates> getLocationUpdates();

    void stopLocationTracking();
}
