
package pl.rmakowiecki.eventhub.model.remote.google;

import java.util.List;

public class OpeningHours {
    public final boolean openNow;
    public final List<Object> weekdayText;

    public OpeningHours(boolean openNow, List<Object> weekdayText) {
        this.openNow = openNow;
        this.weekdayText = weekdayText;
    }
}
