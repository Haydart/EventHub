package pl.rmakowiecki.eventhub.ui.events_map_screen;

import com.jenzz.noop.annotation.NoOp;
import java.util.List;
import pl.rmakowiecki.eventhub.model.local.Event;
import pl.rmakowiecki.eventhub.model.local.LocationCoordinates;
import pl.rmakowiecki.eventhub.ui.BaseView;

@NoOp
interface EventsMapView extends BaseView {
    void showEvents(List<Event> eventsList);

    void initMap();

    void moveMapCamera(LocationCoordinates locationCoordinates);
}
