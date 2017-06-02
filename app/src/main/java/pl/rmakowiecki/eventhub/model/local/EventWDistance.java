package pl.rmakowiecki.eventhub.model.local;

/**
 * Created by m1per on 01.06.2017.
 */

public final class EventWDistance {
    private final Event event;
    private final double distanceCalculable;
    private final String distanceDisplayable;

    public EventWDistance(Event event, double distanceCalculable, String distanceDisplayable) {
        this.event = event;
        this.distanceCalculable = distanceCalculable;
        this.distanceDisplayable = distanceDisplayable;
    }

    public Event getEvent() {
        return event;
    }

    public double getDistanceCalculable() {
        return distanceCalculable;
    }

    public String getDistanceDisplayable() {
        return distanceDisplayable;
    }
}
