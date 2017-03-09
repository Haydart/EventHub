package pl.rmakowiecki.eventhub.repository;

import java.util.List;
import pl.rmakowiecki.eventhub.model.local.Event;
import rx.Observable;

public class EventsRepository implements Repository<Event> {

    @Override
    public void add(Event item) {

    }

    @Override
    public void add(Iterable<Event> items) {

    }

    @Override
    public void update(Event item) {

    }

    @Override
    public void remove(Event item) {

    }

    @Override
    public Observable<List<Event>> query(Specification specification) {
        return null;
    }
}
