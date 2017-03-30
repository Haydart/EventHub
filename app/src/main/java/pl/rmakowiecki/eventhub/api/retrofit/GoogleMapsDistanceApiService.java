package pl.rmakowiecki.eventhub.api.retrofit;

import pl.rmakowiecki.eventhub.api.GoogleApiConstants;
import pl.rmakowiecki.eventhub.model.remote.google.GoogleMapsDistanceApiResponse;
import pl.rmakowiecki.eventhub.model.remote.google.Row;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface GoogleMapsDistanceApiService {
    @GET(GoogleApiConstants.DISTANCE_MATRIX_PATH)
    Observable<GoogleMapsDistanceApiResponse<Row>> getDistanceApiRow(
            @Query("key") String key,
            @Query("origins") String origin,
            @Query("destinations") String destinations
    );
}
