package pl.rmakowiecki.eventhub.ui.screen_event_calendar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import pl.rmakowiecki.eventhub.RxLocationProvider;
import pl.rmakowiecki.eventhub.model.local.Event;
import pl.rmakowiecki.eventhub.model.local.EventWDistance;
import pl.rmakowiecki.eventhub.ui.BasePresenter;
import pl.rmakowiecki.eventhub.util.SortTypes;

import static pl.rmakowiecki.eventhub.ui.screen_event_calendar.EventComparator.DATE_SORT;
import static pl.rmakowiecki.eventhub.ui.screen_event_calendar.EventComparator.DISTANCE_SORT;
import static pl.rmakowiecki.eventhub.ui.screen_event_calendar.EventComparator.ascending;
import static pl.rmakowiecki.eventhub.ui.screen_event_calendar.EventComparator.getComparator;

/**
 * Created by m1per on 18.04.2017.
 */

class MyEventsFragmentPresenter extends BasePresenter<MyEventsFragmentView> {

    private RxLocationProvider provider = new RxLocationProvider();
    private EventsDistanceCalculator calculator = new EventsDistanceCalculator();
    private EventsRepository repository;
    private ArrayList<String> distances = new ArrayList<>();
    private int position;
    private List<EventWDistance> eventsWithDistances = new ArrayList<>();
    private List<Event> allEvents = new ArrayList<>();

    MyEventsFragmentPresenter(int position) {
        repository = new EventsRepository();
        this.position = position;
    }

    private void onViewInitialization() {
        acquireEvents();
    }

    private void acquireEvents() {
        repository
                .query(new MyEventsSpecification(position) {
                })
                .compose(applySchedulers())
                .subscribe(this::updateEventsList);
    }

    private void updateEventsList(List<Event> events) {
        boolean toRemove = false;
        int position = 0;
        if (allEvents.isEmpty()) {
            allEvents = events;
        } else {
            for (Event event : events) {
                toRemove = false;
                for (Event currentEvent : allEvents) {
                    if (currentEvent.getId().equals(event.getId())) {
                        toRemove = true;
                        position = allEvents.indexOf(currentEvent);
                    }
                }
                if (toRemove) {
                    allEvents.remove(position);
                }
                allEvents.add(event);
            }
        }
        passCompleteData();
    }

    private void passCompleteData() {
        provider.getLocation()
                .filter(location -> location != null)
                .compose(applySchedulers())
                .subscribe(
                        coordinates -> {
                            eventsWithDistances = calculator.calculateDistances(coordinates, allEvents);
                            view.showEvents(eventsWithDistances);
                        });
    }

    @Override
    protected void onViewStarted(MyEventsFragmentView view) {
        super.onViewStarted(view);
        onViewInitialization();
    }

    void onSortRequested(SortTypes type) {
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
    public MyEventsFragmentView getNoOpView() {
        return NoOpMyEventsFragmentView.INSTANCE;
    }
}
