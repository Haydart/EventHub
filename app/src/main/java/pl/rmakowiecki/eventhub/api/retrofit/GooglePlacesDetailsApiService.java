package pl.rmakowiecki.eventhub.api.retrofit;

import pl.rmakowiecki.eventhub.api.GoogleApiConstants;
import pl.rmakowiecki.eventhub.model.remote.google.GooglePlacesDetailsApiResponse;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface GooglePlacesDetailsApiService {
    @GET(GoogleApiConstants.PLACE_DETAILS_PATH)
    Observable<GooglePlacesDetailsApiResponse> getPlaceDetails(
            @Query("key") String key,
            @Query("placeid") String placeId);
}
