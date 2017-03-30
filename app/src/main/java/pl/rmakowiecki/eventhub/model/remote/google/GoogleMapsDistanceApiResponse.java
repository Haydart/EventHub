
package pl.rmakowiecki.eventhub.model.remote.google;

import java.util.List;

public class GoogleMapsDistanceApiResponse<R> {
    public List<String> destinationAddresses;
    public List<String> originAddresses;
    public List<R> rows;
    public String status;
}
