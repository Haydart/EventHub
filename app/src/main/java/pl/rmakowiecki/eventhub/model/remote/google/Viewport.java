
package pl.rmakowiecki.eventhub.model.remote.google;

public class Viewport {
    public final Coordinates northeast;
    public final Coordinates southwest;

    public Viewport(Coordinates northeast, Coordinates southwest) {
        this.northeast = northeast;
        this.southwest = southwest;
    }
}
