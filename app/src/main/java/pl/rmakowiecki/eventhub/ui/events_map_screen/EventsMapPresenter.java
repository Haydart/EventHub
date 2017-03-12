package pl.rmakowiecki.eventhub.ui.events_map_screen;

import pl.rmakowiecki.eventhub.api.EventsSpecification;
import pl.rmakowiecki.eventhub.model.local.Event;
import pl.rmakowiecki.eventhub.repository.EventsRepository;
import pl.rmakowiecki.eventhub.repository.Repository;
import pl.rmakowiecki.eventhub.ui.BasePresenter;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

class EventsMapPresenter extends BasePresenter<EventsMapView> {

    private Repository<Event> repository;

    public EventsMapPresenter() {
        repository = new EventsRepository();
    }

    public void onEventsNeeded() {
        repository
                .query(new EventsSpecification() {
                })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(view::showEvents);
    }

    public void onViewInitialization() {
        view.initMap();
    }

    public void onMapViewInitialized() {
        // TODO: 12/03/2017 implement
    }

    @Override
    public EventsMapView getNoOpView() {
        return null;
    }
}
