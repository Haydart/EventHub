
package pl.rmakowiecki.eventhub.model.remote.google;

import java.util.List;

public class AddressComponent {

    public final String longName;
    public final String shortName;
    public final List<String> types;

    public AddressComponent(String longName, String shortName, List<String> types) {
        this.longName = longName;
        this.shortName = shortName;
        this.types = types;
    }
}
