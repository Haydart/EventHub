package pl.rmakowiecki.eventhub.api.retrofit;

import pl.rmakowiecki.eventhub.api.GoogleApiConstants;
import pl.rmakowiecki.eventhub.model.remote.google.GooglePlace;
import pl.rmakowiecki.eventhub.model.remote.google.GooglePlacesApiResponse;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface GooglePlacesApiService {
    @GET(GoogleApiConstants.NEARBY_SEARCH_PATH)
    Observable<GooglePlacesApiResponse<GooglePlace>> getNearbyPlaces(
            @Query("radius") int radius,
            @Query("location") String location,
            @Query("key") String key);

    @GET(GoogleApiConstants.NEARBY_SEARCH_PATH)
    Observable<GooglePlacesApiResponse<GooglePlace>> getAdditionalNearbyPlaces(
            @Query("key") String key,
            @Query("pagetoken") String nextPageToken
    );
}
