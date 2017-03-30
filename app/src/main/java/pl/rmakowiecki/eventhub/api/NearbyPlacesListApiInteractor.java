package pl.rmakowiecki.eventhub.api;

import android.support.annotation.NonNull;
import java.util.List;
import pl.rmakowiecki.eventhub.api.retrofit.GooglePlacesApiService;
import pl.rmakowiecki.eventhub.model.remote.google.GooglePlace;
import pl.rmakowiecki.eventhub.model.remote.google.GooglePlacesApiResponse;
import rx.Observable;
import rx.schedulers.Schedulers;

public class NearbyPlacesListApiInteractor extends BaseApiInteractor implements INearbyPlacesListApiInteractor {
    private GooglePlacesApiService nearbyPlacesService;

    private Observable<GooglePlacesApiResponse<GooglePlace>> placesResponseObservable;
    private Observable<GooglePlacesApiResponse<GooglePlace>> additionalPlacesResponseObservable;

    private String nextResultPageToken = "";
    private boolean isNextItemsPageAvailable = true;

    public NearbyPlacesListApiInteractor() {
        super(GoogleApiConstants.PLACES_API_BASE_URL);
        nearbyPlacesService = retrofit.create(GooglePlacesApiService.class);
    }

    @Override
    public Observable<List<GooglePlace>> loadGooglePlaces(String location) {
        placesResponseObservable = nearbyPlacesService.getNearbyPlaces(GoogleApiConstants.PLACE_SEARCH_RADIUS, location, GoogleApiConstants.API_KEY);
        return placesResponseObservable
                .observeOn(Schedulers.computation())
                .map(this::interceptNextResultPageToken)
                .flatMap(this::retrieveModelFromResponse);
    }

    @Override
    public Observable<List<GooglePlace>> loadAdditionalGooglePlaces(String location) {
        additionalPlacesResponseObservable = nearbyPlacesService.getAdditionalNearbyPlaces(GoogleApiConstants.API_KEY, nextResultPageToken);
        return additionalPlacesResponseObservable
                .observeOn(Schedulers.computation())
                .map(this::interceptNextResultPageToken)
                .flatMap(this::retrieveModelFromResponse);
    }

    @NonNull
    private GooglePlacesApiResponse<GooglePlace> interceptNextResultPageToken(GooglePlacesApiResponse<GooglePlace> placesApiResponse) {
        nextResultPageToken = placesApiResponse.nextPageToken;
        isNextItemsPageAvailable = nextResultPageToken != null;
        return placesApiResponse;
    }

    @NonNull
    private Observable<List<GooglePlace>> retrieveModelFromResponse(GooglePlacesApiResponse<GooglePlace> googlePlacesApiResponse) {
        return Observable.just(googlePlacesApiResponse.results);
    }
}
