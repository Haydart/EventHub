
package pl.rmakowiecki.eventhub.model.remote.google;

public class Period {

    public final Close close;
    public final Open open;

    public Period(Close close, Open open) {
        this.close = close;
        this.open = open;
    }
}
