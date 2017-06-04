package pl.rmakowiecki.eventhub.ui.screen_event_calendar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import pl.rmakowiecki.eventhub.RxLocationProvider;
import pl.rmakowiecki.eventhub.model.local.Event;
import pl.rmakowiecki.eventhub.model.local.EventWDistance;
import pl.rmakowiecki.eventhub.repository.Repository;
import pl.rmakowiecki.eventhub.ui.BasePresenter;
import pl.rmakowiecki.eventhub.util.SortTypes;

import static pl.rmakowiecki.eventhub.ui.screen_event_calendar.EventComparator.DATE_SORT;
import static pl.rmakowiecki.eventhub.ui.screen_event_calendar.EventComparator.DISTANCE_SORT;
import static pl.rmakowiecki.eventhub.ui.screen_event_calendar.EventComparator.ascending;
import static pl.rmakowiecki.eventhub.ui.screen_event_calendar.EventComparator.getComparator;

/**
 * Created by m1per on 18.04.2017.
 */

public class EventsFragmentPresenter extends BasePresenter<EventsFragmentView> {

    private RxLocationProvider provider = new RxLocationProvider();
    private EventsDistanceCalculator calculator = new EventsDistanceCalculator();
    private EventsRepository repository;
    private int position;
    private List<EventWDistance> eventsWithDistances = new ArrayList<>();

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
                            eventsWithDistances = calculator.calculateDistances(coordinates, events);
                            view.showEvents(eventsWithDistances);
                        });
    }

    @Override
    protected void onViewStarted(EventsFragmentView view) {
        super.onViewStarted(view);
        onViewInitialization();
    }

    public void onSortRequested(SortTypes type) {
        switch (type) {
            case DISTANCE_SORT:
                Collections.sort(eventsWithDistances, ascending(getComparator(DISTANCE_SORT)));
                break;
            case DATE_SORT:
                Collections.sort(eventsWithDistances, ascending(getComparator(DATE_SORT)));
                break;
        }
        view.showEvents(eventsWithDistances);
    }

    @Override
    public EventsFragmentView getNoOpView() {
        return NoOpEventsFragmentView.INSTANCE;
    }
}
