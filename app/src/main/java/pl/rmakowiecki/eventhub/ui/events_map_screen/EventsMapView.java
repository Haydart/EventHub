package pl.rmakowiecki.eventhub.ui.events_map_screen;

import java.util.List;
import pl.rmakowiecki.eventhub.model.local.Event;
import pl.rmakowiecki.eventhub.ui.BaseView;

interface EventsMapView extends BaseView {
    void showEvents(List<Event> eventsList);

    void initMap();
}
