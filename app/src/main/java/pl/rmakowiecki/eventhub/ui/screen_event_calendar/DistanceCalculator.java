package pl.rmakowiecki.eventhub.ui.screen_event_calendar;

import java.util.List;
import pl.rmakowiecki.eventhub.model.local.Event;
import pl.rmakowiecki.eventhub.model.local.LocationCoordinates;

/**
 * Created by m1per on 17.05.2017.
 */

public interface DistanceCalculator {

    public List calculateDistances(LocationCoordinates coordinates, List<Event> events);
}
