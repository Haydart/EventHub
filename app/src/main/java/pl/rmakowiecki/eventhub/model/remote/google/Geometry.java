
package pl.rmakowiecki.eventhub.model.remote.google;

public class Geometry {
    public final Coordinates location;
    public final Viewport viewport;

    public Geometry(Coordinates location, Viewport viewport) {
        this.location = location;
        this.viewport = viewport;
    }
}
