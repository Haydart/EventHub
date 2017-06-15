package pl.rmakowiecki.eventhub.ui.screen_event_calendar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import pl.rmakowiecki.eventhub.model.local.Event;
import pl.rmakowiecki.eventhub.model.local.User;
import pl.rmakowiecki.eventhub.model.remote.RemoteEvent;
import pl.rmakowiecki.eventhub.repository.ModelMapper;
import pl.rmakowiecki.eventhub.ui.screen_preference_categories.PreferenceCategory;

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
                mapToRemoteTagList(model.getEventTags()),
                convertToMapRepresentation(model.getUsers())
        );
    }

    private Map<String, String> convertToMapRepresentation(List<User> userList) {
        Map<String, String> result = new HashMap<>(userList.size());
        for (User user : userList) {
            result.put(user.getId(), user.getName());
        }
        return result;
    }

    private Map<String, List<String>> mapToRemoteTagList(List<PreferenceCategory> eventTags) {
        Map<String, List<String>> categories = new HashMap<>();
        for (PreferenceCategory category : eventTags) {
            categories.put(category.getTitle(), category.getChildList());
        }
        return categories;
    }
}
