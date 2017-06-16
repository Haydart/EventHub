package pl.rmakowiecki.eventhub.ui.screen_event_calendar;

import java.util.List;
import pl.rmakowiecki.eventhub.model.local.Event;
import pl.rmakowiecki.eventhub.model.remote.OperationStatus;
import pl.rmakowiecki.eventhub.model.remote.RemoteEvent;
import pl.rmakowiecki.eventhub.repository.ModelMapper;
import pl.rmakowiecki.eventhub.repository.QueryList;
import pl.rmakowiecki.eventhub.repository.Repository;
import pl.rmakowiecki.eventhub.repository.Specification;
import rx.Observable;

/**
 * Created by m1per on 17.04.2017.
 */

public class EventsRepository implements Repository<Event>, QueryList<Event> {

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
        MyEventsSpecifications spec = (MyEventsSpecifications) specification;
        return eventDBInteractor
                .getData(spec.getTabPosition())
                .filter(event -> event != null)
                .buffer(200, java.util.concurrent.TimeUnit.MILLISECONDS)
                .filter(events -> !events.isEmpty());
    }
}
