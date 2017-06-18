package pl.rmakowiecki.eventhub.ui.screen_event_calendar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import pl.rmakowiecki.eventhub.RxLocationProvider;
import pl.rmakowiecki.eventhub.model.local.Event;
import pl.rmakowiecki.eventhub.model.local.EventWDistance;
import pl.rmakowiecki.eventhub.ui.BasePresenter;
import pl.rmakowiecki.eventhub.util.PreferencesManager;
import pl.rmakowiecki.eventhub.util.SortTypes;

import static pl.rmakowiecki.eventhub.ui.screen_event_calendar.EventComparator.DATE_SORT;
import static pl.rmakowiecki.eventhub.ui.screen_event_calendar.EventComparator.DISTANCE_SORT;
import static pl.rmakowiecki.eventhub.ui.screen_event_calendar.EventComparator.ascending;
import static pl.rmakowiecki.eventhub.ui.screen_event_calendar.EventComparator.getComparator;

/**
 * Created by m1per on 17.06.2017.
 */

public class PersonalizedEventsFragmentPresenter extends BasePresenter<PersonalizedEventsFragmentView> {

    private RxLocationProvider provider = new RxLocationProvider();
    private EventsDistanceCalculator calculator = new EventsDistanceCalculator();
    private EventsRepository repository;
    private List<String> distances = new ArrayList<>();
    private int tabPosition;
    private List<EventWDistance> eventsWithDistances = new ArrayList<>();
    private List<Event> allEvents = new ArrayList<>();
    private PreferencesManager preferencesManager;

    PersonalizedEventsFragmentPresenter(int tabPosition, PreferencesManager preferencesManager) {
        repository = new EventsRepository(preferencesManager.getInterestsList());
        this.tabPosition = tabPosition;
        this.preferencesManager = preferencesManager;
    }

    private void onViewInitialization() {
        acquireEvents();
    }

    private void acquireEvents() {
        repository
                .query(new MyEventsSpecification(tabPosition) {
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
    protected void onViewStarted(PersonalizedEventsFragmentView view) {
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

    public void addEventParticipant(String eventId, int position) {
        repository.updateEventParticipants(eventId)
                .subscribe(operationStatus -> view.showActionStatus(operationStatus, position));
    }

    @Override
    public PersonalizedEventsFragmentView getNoOpView() {
        return NoOpPersonalizedEventsFragmentView.INSTANCE;
    }
}
