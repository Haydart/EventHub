package pl.rmakowiecki.eventhub.ui.calendar_screen;

import pl.rmakowiecki.eventhub.model.local.Event;
import pl.rmakowiecki.eventhub.repository.Repository;
import pl.rmakowiecki.eventhub.ui.BasePresenter;
import pl.rmakowiecki.eventhub.ui.events_map_screen.EventsSpecification;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by m1per on 18.04.2017.
 */

public class EventsFragmentPresenter extends BasePresenter<EventsFragmentView> {

    private Repository<Event> repository;

    EventsFragmentPresenter() {
        repository = new EventsRepository();
    }

    private void onViewInitialization() {
        repository
                .query(new EventsSpecification() {
                })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(view::showEvents);
    }

    @Override
    protected void onViewStarted(EventsFragmentView view) {
        super.onViewStarted(view);
        onViewInitialization();
    }

    @Override
    public EventsFragmentView getNoOpView() {
        return NoOpEventsFragmentView.INSTANCE;
    }
}
