package pl.rmakowiecki.eventhub.ui.screen_event_calendar;

import java.util.List;
import pl.rmakowiecki.eventhub.api.EventsDatabaseInteractor;
import pl.rmakowiecki.eventhub.model.local.Event;
import pl.rmakowiecki.eventhub.model.mappers.EventMapper;
import pl.rmakowiecki.eventhub.model.mappers.ModelMapper;
import pl.rmakowiecki.eventhub.model.remote.RemoteEvent;
import pl.rmakowiecki.eventhub.repository.QueryList;
import pl.rmakowiecki.eventhub.repository.Repository;
import pl.rmakowiecki.eventhub.repository.Specification;
import rx.Observable;

/**
 * Created by m1per on 17.04.2017.
 */

public class EventsRepository implements Repository<Event>, QueryList<Event> {

    private EventsDatabaseInteractor databaseInteractor;
    private ModelMapper<Event, RemoteEvent> eventMapper = new EventMapper();

    public EventsRepository() {
        databaseInteractor = new EventsDatabaseInteractor();
    }

    @Override
    public void add(Event item) {
        databaseInteractor.addEvent(eventMapper.map(item));
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
        return databaseInteractor.getData(spec.getTabPosition());
    }
}
