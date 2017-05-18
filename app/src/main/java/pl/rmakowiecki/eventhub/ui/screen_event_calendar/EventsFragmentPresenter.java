package pl.rmakowiecki.eventhub.ui.screen_event_calendar;

import java.util.ArrayList;
import java.util.List;
import pl.rmakowiecki.eventhub.RxLocationProvider;
import pl.rmakowiecki.eventhub.model.local.Event;
import pl.rmakowiecki.eventhub.model.local.LocationCoordinates;
import pl.rmakowiecki.eventhub.repository.Repository;
import pl.rmakowiecki.eventhub.ui.BasePresenter;

/**
 * Created by m1per on 18.04.2017.
 */

public class EventsFragmentPresenter extends BasePresenter<EventsFragmentView> {

    private RxLocationProvider provider = new RxLocationProvider();
    private EventsDistanceCalculator calculator = new EventsDistanceCalculator();
    private Repository<Event> repository;
    private int position;
    private ArrayList<String> distances = new ArrayList<>();

    EventsFragmentPresenter(int position) {
        repository = new EventsRepository();
        this.position = position;
    }

    private void onViewInitialization() {
        acquireEvents();
    }

    private void acquireEvents() {
        repository
                .query(new MyEventsSpecifications(position) {
                })
                .compose(applySchedulers())
                .subscribe(this::passCompleteData);
    }

    private void passCompleteData(List<Event> events) {
        provider.getLocation()
                .filter(location -> location != null)
                .compose(applySchedulers())
                .subscribe(
                        coordinates -> {
                            distances = calculator.calculateDistances(coordinates, events);
                            view.showEvents(events, distances);
                        });
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
