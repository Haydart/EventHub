package pl.rmakowiecki.eventhub;

import pl.rmakowiecki.eventhub.model.local.LocationCoordinates;
import pl.rmakowiecki.eventhub.ui.screen_events_map.StatusWrapper;
import rx.Observable;

public interface LocationProvider {
    Observable<StatusWrapper> isLocationTurnedOn();

    Observable<LocationCoordinates> getLocation();

    Observable<LocationCoordinates> getLocationUpdates();

    void stopLocationTracking();
}
