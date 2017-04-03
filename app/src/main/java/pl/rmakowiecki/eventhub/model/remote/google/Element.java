
package pl.rmakowiecki.eventhub.model.remote.google;

public class Element {
    public final Distance distance;
    public final Duration duration;
    public final String status;

    public Element(Distance distance, Duration duration, String status) {
        this.distance = distance;
        this.duration = duration;
        this.status = status;
    }
}
