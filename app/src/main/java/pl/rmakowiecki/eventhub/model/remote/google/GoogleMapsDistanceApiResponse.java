
package pl.rmakowiecki.eventhub.model.remote.google;

import java.util.List;

public class GoogleMapsDistanceApiResponse<R> {
    public final List<String> destinationAddresses;
    public final List<String> originAddresses;
    public final List<R> rows;
    public final String status;

    public GoogleMapsDistanceApiResponse(List<String> destinationAddresses, List<String> originAddresses, List<R> rows, String status) {
        this.destinationAddresses = destinationAddresses;
        this.originAddresses = originAddresses;
        this.rows = rows;
        this.status = status;
    }
}
