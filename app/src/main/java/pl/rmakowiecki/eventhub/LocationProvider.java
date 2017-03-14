package pl.rmakowiecki.eventhub;

import pl.rmakowiecki.eventhub.model.local.LocationCoordinates;
import rx.Observable;

public interface LocationProvider {
    Observable<LocationCoordinates> getLocation();

    Observable<LocationCoordinates> getLocationUpdates();

    void stopLocationTracking();
}
