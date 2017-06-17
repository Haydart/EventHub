package pl.rmakowiecki.eventhub.ui.screen_event_calendar;

import java.util.List;
import pl.rmakowiecki.eventhub.api.EventsDatabaseInteractor;
import pl.rmakowiecki.eventhub.model.local.Event;
import pl.rmakowiecki.eventhub.model.mappers.EventMapper;
import pl.rmakowiecki.eventhub.model.mappers.ModelMapper;
import pl.rmakowiecki.eventhub.model.remote.OperationStatus;
import pl.rmakowiecki.eventhub.model.remote.RemoteEvent;
import pl.rmakowiecki.eventhub.repository.QueryList;
import pl.rmakowiecki.eventhub.repository.Repository;
import pl.rmakowiecki.eventhub.repository.Specification;
import pl.rmakowiecki.eventhub.ui.screen_preference_categories.PreferenceCategory;
import rx.Observable;

/**
 * Created by m1per on 17.04.2017.
 */

public class EventsRepository implements Repository<Event>, QueryList<Event> {

    public static final int BUFFER_TIMESPAN = 200;

    private EventsDatabaseInteractor eventDBInteractor;
    private EventParticipantsDatabaseInteractor eventPatricipantsDBInteractor;
    private ModelMapper<Event, RemoteEvent> eventMapper = new EventMapper();

    public EventsRepository() {

        eventDBInteractor = new EventsDatabaseInteractor();
        eventPatricipantsDBInteractor = new EventParticipantsDatabaseInteractor();
    }

    @Override
    public void add(Event item) {
        eventDBInteractor.addEvent(eventMapper.map(item));
    }

    public Observable<OperationStatus> updateEventParticipants(String eventId) {
        return eventPatricipantsDBInteractor
                .addEventParticipant(eventId);
    }

    @Override
    public void add(Iterable<Event> items) {
        //no-op
    }

    @Override
    public void update(Event item) {
        //no-op
    }

    @Override
    public void remove(Event item) {
        //no-op
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

    public Observable<List<Event>> query(Specification specification, List<PreferenceCategory> preferencesList) {
        MyEventsSpecification spec = (MyEventsSpecification) specification;
        return eventDBInteractor
                .getData(spec.getTabPosition(), preferencesList)
                .filter(event -> event != null)
                .buffer(BUFFER_TIMESPAN, java.util.concurrent.TimeUnit.MILLISECONDS)
                .filter(events -> !events.isEmpty());
    }
}
