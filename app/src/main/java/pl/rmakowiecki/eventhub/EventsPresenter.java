package pl.rmakowiecki.eventhub;

import java.util.List;
import pl.rmakowiecki.eventhub.api.EventsSpecification;
import pl.rmakowiecki.eventhub.model.local.Event;
import pl.rmakowiecki.eventhub.repository.EventsRepository;
import pl.rmakowiecki.eventhub.repository.Repository;
import pl.rmakowiecki.eventhub.ui.BasePresenter;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

class EventsPresenter extends BasePresenter<EventsView> {

    private Repository<Event> repository;

    public EventsPresenter() {
        repository = new EventsRepository();
    }

    public void onEventsNeeded() {
        repository
                .query(new EventsSpecification() {})
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map(this::doSomeComputation)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(view::showEvents);
    }

    private List<Event> doSomeComputation(List<Event> events) {
        return events;
    }

    @Override
    public EventsView getNoOpView() {
        return null;
    }
}
