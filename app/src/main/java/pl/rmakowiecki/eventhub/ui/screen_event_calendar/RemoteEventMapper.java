package pl.rmakowiecki.eventhub.ui.screen_event_calendar;

import java.util.ArrayList;
import pl.rmakowiecki.eventhub.model.local.Event;
import pl.rmakowiecki.eventhub.model.remote.RemoteEvent;
import pl.rmakowiecki.eventhub.repository.ModelMapper;

class RemoteEventMapper implements ModelMapper<RemoteEvent, Event> {
    @Override
    public Event map(RemoteEvent model) {
        return new Event(
                "",
                model.getName(),
                model.getDescription(),
                model.getTimestamp(),
                model.getOrganizer(),
                model.getAddress(),
                model.getLocationCoordinates(),
                new ArrayList<>(),
                new ArrayList<>()
        );
    }
}
