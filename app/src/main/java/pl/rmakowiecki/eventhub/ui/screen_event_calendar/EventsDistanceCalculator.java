package pl.rmakowiecki.eventhub.ui.screen_event_calendar;

import android.location.Location;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import pl.rmakowiecki.eventhub.model.local.Event;
import pl.rmakowiecki.eventhub.model.local.LocationCoordinates;

/**
 * Created by m1per on 17.05.2017.
 */

public class EventsDistanceCalculator implements DistanceCalculator {

    @Override
    public ArrayList<String> calculateDistances(LocationCoordinates coordinates, List<Event> events) {
        ArrayList<String> distances = new ArrayList<>();
        Location userLocation = new Location("");
        String distance;
        double distanceInMeters;
        double calculatedDistance;
        String eventCoords[];

        userLocation.setLatitude(coordinates.getLatitude());
        userLocation.setLongitude(coordinates.getLongitude());
        for (Event event : events) {
            eventCoords = event.getCoordinates().split(",");
            Location eventLocation = new Location("");
            eventLocation.setLatitude(Double.parseDouble(eventCoords[0]));
            eventLocation.setLongitude(Double.parseDouble(eventCoords[1]));

            distanceInMeters = (userLocation.distanceTo(eventLocation));
            if (distanceInMeters < 1000) {
                distance = Integer.toString((int) distanceInMeters);
                distances.add(distance + " m");
            } else {
                calculatedDistance = BigDecimal.valueOf(distanceInMeters / 1000)
                        .setScale(1, RoundingMode.HALF_UP)
                        .doubleValue();
                distance = Double.toString(calculatedDistance);
                distances.add(distance + " km");
            }
        }
        return distances;
    }
}
