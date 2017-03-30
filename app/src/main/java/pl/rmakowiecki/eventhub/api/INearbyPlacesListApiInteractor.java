package pl.rmakowiecki.eventhub.api;

import java.util.List;
import pl.rmakowiecki.eventhub.model.remote.google.GooglePlace;
import rx.Observable;

public interface INearbyPlacesListApiInteractor {
    Observable<List<GooglePlace>> loadGooglePlaces(String location);

    Observable<List<GooglePlace>> loadAdditionalGooglePlaces(String location);
}
