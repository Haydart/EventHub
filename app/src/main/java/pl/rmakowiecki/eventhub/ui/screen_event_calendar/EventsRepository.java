package pl.rmakowiecki.eventhub.ui.screen_event_calendar;

import java.util.List;
import pl.rmakowiecki.eventhub.api.EventsDatabaseInteractor;
import pl.rmakowiecki.eventhub.model.local.Event;
import pl.rmakowiecki.eventhub.model.mappers.EventMapper;
import pl.rmakowiecki.eventhub.model.mappers.ModelMapper;
import pl.rmakowiecki.eventhub.model.remote.RemoteEvent;
import pl.rmakowiecki.eventhub.repository.QueryList;
import pl.rmakowiecki.eventhub.repository.QueryStatus;
import pl.rmakowiecki.eventhub.repository.Repository;
import pl.rmakowiecki.eventhub.repository.Specification;
import rx.Observable;

/**
 * Created by m1per on 17.04.2017.
 */

public class EventsRepository implements Repository<Event, QueryStatus>, QueryList<Event> {

    public static final int BUFFER_TIMESPAN = 200;

    private EventsDatabaseInteractor eventDBInteractor;
    private EventParticipantsDatabaseInteractor eventPatricipantsDBInteractor;
    private ModelMapper<Event, RemoteEvent> eventMapper = new EventMapper();

    public EventsRepository() {

        eventDBInteractor = new EventsDatabaseInteractor();
        eventPatricipantsDBInteractor = new EventParticipantsDatabaseInteractor();
    }

    @Override
    public Observable<QueryStatus> add(Event item) {
        return eventDBInteractor.addEvent(eventMapper.map(item));
    }

    public Observable<QueryStatus> updateEventParticipants(String eventId) {
        return eventPatricipantsDBInteractor
                .addEventParticipant(eventId);
    }

    @Override
    public Observable<QueryStatus> add(Iterable<Event> items) {
        return Observable.empty();
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
}
