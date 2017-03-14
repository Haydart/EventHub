package pl.rmakowiecki.eventhub.repository;

import java.util.List;
import pl.rmakowiecki.eventhub.model.local.Event;
import rx.Observable;

public class EventsRepository implements Repository<Event> {

    @Override
    public void add(Event item) {
        // TODO: 14/03/2017 implement
    }

    @Override
    public void add(Iterable<Event> items) {
        // TODO: 14/03/2017 implement
    }

    @Override
    public void update(Event item) {
        // TODO: 14/03/2017 implement
    }

    @Override
    public void remove(Event item) {
        // TODO: 14/03/2017 implement
    }

    @Override
    public Observable<List<Event>> query(Specification specification) {
        return Observable.empty();
    }
}
