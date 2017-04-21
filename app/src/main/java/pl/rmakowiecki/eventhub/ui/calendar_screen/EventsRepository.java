package pl.rmakowiecki.eventhub.ui.calendar_screen;

import java.util.List;
import pl.rmakowiecki.eventhub.model.local.Event;
import pl.rmakowiecki.eventhub.repository.Repository;
import pl.rmakowiecki.eventhub.repository.Specification;
import rx.Observable;

/**
 * Created by m1per on 17.04.2017.
 */

class EventsRepository implements Repository<Event> {
    @Override
    public void add(Event item) {
        //no-op
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
        return new EventsDatabaseInteractor().getData();
    }
}
