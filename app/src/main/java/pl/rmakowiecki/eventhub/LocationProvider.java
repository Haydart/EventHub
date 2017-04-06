package pl.rmakowiecki.eventhub;

import com.google.android.gms.common.api.Status;
import pl.rmakowiecki.eventhub.model.local.LocationCoordinates;
import rx.Observable;

public interface LocationProvider {
    Observable<Status> isLocationTurnedOn();

    Observable<LocationCoordinates> getLocation();

    Observable<LocationCoordinates> getLocationUpdates();

    void stopLocationTracking();
}
