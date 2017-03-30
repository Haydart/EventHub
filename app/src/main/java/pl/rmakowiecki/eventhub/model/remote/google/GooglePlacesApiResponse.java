package pl.rmakowiecki.eventhub.model.remote.google;

import java.util.List;

public class GooglePlacesApiResponse<R> {
    public final List<Object> htmlAttributions;
    public final String nextPageToken;
    public final List<R> results;
    public final String status;

    public GooglePlacesApiResponse(List<Object> htmlAttributions, String nextPageToken, List<R> results, String status) {
        this.htmlAttributions = htmlAttributions;
        this.nextPageToken = nextPageToken;
        this.results = results;
        this.status = status;
    }
}
