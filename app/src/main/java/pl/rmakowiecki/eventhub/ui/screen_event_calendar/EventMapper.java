package pl.rmakowiecki.eventhub.ui.screen_event_calendar;

import pl.rmakowiecki.eventhub.model.local.Event;
import pl.rmakowiecki.eventhub.model.remote.RemoteEvent;
import pl.rmakowiecki.eventhub.repository.ModelMapper;

class EventMapper implements ModelMapper<Event, RemoteEvent> {
    @Override
    public RemoteEvent map(Event model) {
        return new RemoteEvent(
                model.getName(),
                model.getDescription(),
                model.getTimestamp(),
                model.getOrganizer(),
                model.getAddress(),
                model.getLocationCoordinates(),
                model.getUsers()
        );
    }
}
