package pl.rmakowiecki.eventhub.ui.screen_event_calendar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pl.rmakowiecki.eventhub.model.local.Event;
import pl.rmakowiecki.eventhub.model.local.EventAttendee;

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
                convertToListRepresentation(model.getAttendees())
        );
    }

    private List<EventAttendee> convertToListRepresentation(Map<String, String> attendeeMap) {
        List<EventAttendee> result = new ArrayList<>(attendeeMap.size());
        for (Map.Entry<String, String> attendee : attendeeMap.entrySet()) {
            result.add(new EventAttendee(attendee.getKey(), attendee.getValue()));
        }

        return result;
    }
}
