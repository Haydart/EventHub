package pl.rmakowiecki.eventhub.ui.screen_event_calendar;

import java.util.List;
import pl.rmakowiecki.eventhub.api.EventsDatabaseInteractor;
import pl.rmakowiecki.eventhub.model.local.Event;
import pl.rmakowiecki.eventhub.model.mappers.EventMapper;
import pl.rmakowiecki.eventhub.model.mappers.ModelMapper;
import pl.rmakowiecki.eventhub.model.remote.RemoteEvent;
import pl.rmakowiecki.eventhub.repository.AddOperationRepository;
import pl.rmakowiecki.eventhub.repository.GenericQueryStatus;
import pl.rmakowiecki.eventhub.repository.QueryList;
import pl.rmakowiecki.eventhub.repository.Specification;
import pl.rmakowiecki.eventhub.ui.screen_preference_categories.PreferenceCategory;
import rx.Observable;

/**
 * Created by m1per on 17.04.2017.
 */

public class EventsRepository implements AddOperationRepository<Event, GenericQueryStatus>, QueryList<Event> {

    public static final int BUFFER_TIMESPAN = 200;

    private EventsDatabaseInteractor eventDBInteractor;
    private EventParticipantsDatabaseInteractor eventPatricipantsDBInteractor;
    private UsersEventsDatabaseInteractor userEventsDBInteractor;
    private ModelMapper<Event, RemoteEvent> eventMapper = new EventMapper();

    public EventsRepository() {
        eventDBInteractor = new EventsDatabaseInteractor();
        eventPatricipantsDBInteractor = new EventParticipantsDatabaseInteractor();
        userEventsDBInteractor = new UsersEventsDatabaseInteractor();

    }

    public EventsRepository(List<PreferenceCategory> interestsList) {
        eventDBInteractor = new EventsDatabaseInteractor(interestsList);
        userEventsDBInteractor = new UsersEventsDatabaseInteractor();
        eventPatricipantsDBInteractor = new EventParticipantsDatabaseInteractor();
    }

    @Override
    public Observable<GenericQueryStatus> add(Event item) {
        return eventDBInteractor.addEvent(eventMapper.map(item));
    }

    public Observable<GenericQueryStatus> updateEventParticipants(String eventId, String userDisplayedName) {
        return eventPatricipantsDBInteractor
                .addEventParticipant(eventId, userDisplayedName);
    }

    public Observable<GenericQueryStatus> removeEventParticipant(String eventId) {
        return eventPatricipantsDBInteractor
                .removeUserFromEvent(eventId);
    }

    @Override
    public Observable<GenericQueryStatus> add(Iterable<Event> items) {
        return Observable.empty();
    }

    public Observable<List<Event>> queryForUserEvents() {
        return userEventsDBInteractor
                .getData()
                .filter(event -> event != null)
                .buffer(BUFFER_TIMESPAN, java.util.concurrent.TimeUnit.MILLISECONDS)
                .filter(events -> !events.isEmpty());
    }

    public String getLastReferenceKey() {
        return eventDBInteractor.getReferenceKey();
    }

    @Override
    public Observable<List<Event>> query(Specification specification) {
        MyEventsSpecification spec = (MyEventsSpecification) specification;
        return eventDBInteractor
                .getData(spec.getTabPosition())
                .filter(event -> event != null)
                .buffer(BUFFER_TIMESPAN, java.util.concurrent.TimeUnit.MILLISECONDS)
                .filter(events -> !events.isEmpty());
    }
}
