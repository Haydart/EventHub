package pl.rmakowiecki.eventhub.model.mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import pl.rmakowiecki.eventhub.model.local.Event;
import pl.rmakowiecki.eventhub.model.local.EventAttendee;
import pl.rmakowiecki.eventhub.model.remote.RemoteEvent;
import pl.rmakowiecki.eventhub.ui.screen_preference_categories.PreferenceCategory;

public class RemoteEventMapper implements ModelMapper<RemoteEvent, Event> {
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
                tagToPreferenceCategory(model.getEventTags()),
                convertToListRepresentation(model.getAttendees())
        );
    }

    public Event map(RemoteEvent model, String id) {
        return new Event(
                id,
                model.getName(),
                model.getDescription(),
                model.getTimestamp(),
                model.getOrganizer(),
                model.getAddress(),
                model.getLocationCoordinates(),
                tagToPreferenceCategory(model.getEventTags()),
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

    private List<PreferenceCategory> tagToPreferenceCategory(Map<String, List<String>> tagMap) {
        List<PreferenceCategory> result = new ArrayList<>(tagMap.size());
        for (Map.Entry<String, List<String>> tag : tagMap.entrySet()) {
            result.add(new PreferenceCategory(tag.getKey(), "", tag.getValue()));
        }

        return result;
    }
}
