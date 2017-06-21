package pl.rmakowiecki.eventhub.ui.screen_event_calendar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import pl.rmakowiecki.eventhub.RxLocationProvider;
import pl.rmakowiecki.eventhub.model.local.Event;
import pl.rmakowiecki.eventhub.model.local.EventAttendee;
import pl.rmakowiecki.eventhub.model.local.EventWDistance;
import pl.rmakowiecki.eventhub.ui.BasePresenter;
import pl.rmakowiecki.eventhub.util.SortTypes;
import pl.rmakowiecki.eventhub.util.UserAuthManager;

import static pl.rmakowiecki.eventhub.ui.screen_event_calendar.EventComparator.DATE_SORT;
import static pl.rmakowiecki.eventhub.ui.screen_event_calendar.EventComparator.DISTANCE_SORT;
import static pl.rmakowiecki.eventhub.ui.screen_event_calendar.EventComparator.ascending;
import static pl.rmakowiecki.eventhub.ui.screen_event_calendar.EventComparator.getComparator;

/**
 * Created by m1per on 18.04.2017.
 */

class MyEventsFragmentPresenter extends BasePresenter<MyEventsFragmentView> {

    UserAuthManager userManager = new UserAuthManager();
    private RxLocationProvider provider = new RxLocationProvider();
    private EventsDistanceCalculator calculator = new EventsDistanceCalculator();
    private EventsRepository repository;
    private ArrayList<String> distances = new ArrayList<>();
    private int position;
    private List<EventWDistance> eventsWithDistances = new ArrayList<>();
    private List<Event> allEvents = new ArrayList<>();
    private List<Boolean> attendance = new ArrayList<>();
    private UserAuthManager userAuthManager = new UserAuthManager();
    private SortTypes sortType = SortTypes.DATE_SORT;


    MyEventsFragmentPresenter(int position) {
        repository = new EventsRepository();
        this.position = position;
    }

    private void onViewInitialization() {
        acquireEvents();
    }

    private void acquireEvents() {
        repository
                .queryForUserEvents()
                .compose(applySchedulers())
                .subscribe(this::updateEventsList);
    }

    private void updateEventsList(List<Event> events) {
        boolean toRemove;
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
                if (isUserAttending(event, userManager.getCurrentUserId())) {
                    allEvents.add(event);
                }

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
                            sortEvents();
                            setAttendance();
                            view.showEvents(eventsWithDistances, attendance);
                        });
    }

    private void setAttendance() {
        String userId = userAuthManager.getCurrentUserId();
        Boolean userAttendance;
        for (EventWDistance event : eventsWithDistances) {
            userAttendance = false;
            for (EventAttendee attendee : event.getEvent().getAttendees()) {
                if (attendee.getId().equals(userId)) {
                    userAttendance = true;
                }
            }
            attendance.add(userAttendance);
        }
    }

    private boolean isUserAttending(Event event, String currentUserId) {
        for (EventAttendee attendee : event.getAttendees()) {
            if (attendee.getId().equals(currentUserId)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onViewStarted(MyEventsFragmentView view) {
        super.onViewStarted(view);
        onViewInitialization();
    }

    void onSortRequested(SortTypes type) {
        sortType = type;
        sortEvents();
        view.showEvents(eventsWithDistances, attendance);
    }

    private void sortEvents() {
        switch (sortType) {
            case DISTANCE_SORT:
                Collections.sort(eventsWithDistances, ascending(getComparator(DISTANCE_SORT)));
                break;
            case DATE_SORT:
                Collections.sort(eventsWithDistances, ascending(getComparator(DATE_SORT)));
                break;
        }
    }

    public void removeEventParticipant(String eventId) {
        repository.removeEventParticipant(eventId)
                .subscribe(operationStatus -> view.showActionStatus(operationStatus));
    }

    @Override
    public MyEventsFragmentView getNoOpView() {
        return NoOpMyEventsFragmentView.INSTANCE;
    }
}
