package pl.rmakowiecki.eventhub.model.remote.google;

import java.util.List;

public class GooglePlacesDetailsApiResponse {
    public final List<Object> htmlAttributions;
    public final GooglePlaceDetails result;
    public final String status;

    public GooglePlacesDetailsApiResponse(List<Object> htmlAttributions, GooglePlaceDetails result, String status) {
        this.htmlAttributions = htmlAttributions;
        this.result = result;
        this.status = status;
    }
}
