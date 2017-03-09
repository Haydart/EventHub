package pl.rmakowiecki.eventhub.api;

import java.util.List;
import pl.rmakowiecki.eventhub.model.local.Event;

public class EventsProvider implements DataProvider<Event> {

    @Override
    public Event fetch() {
        return null;
    }

    @Override
    public List<Event> fetchList() {
        return null;
    }
}
