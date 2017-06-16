package pl.rmakowiecki.eventhub.ui.screen_event_calendar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import pl.rmakowiecki.eventhub.RxLocationProvider;
import pl.rmakowiecki.eventhub.model.local.Event;
import pl.rmakowiecki.eventhub.model.local.EventWDistance;
import pl.rmakowiecki.eventhub.model.remote.OperationStatus;
import pl.rmakowiecki.eventhub.ui.BasePresenter;
import pl.rmakowiecki.eventhub.util.SortTypes;
import rx.functions.Action1;

import static pl.rmakowiecki.eventhub.ui.screen_event_calendar.EventComparator.DATE_SORT;
import static pl.rmakowiecki.eventhub.ui.screen_event_calendar.EventComparator.DISTANCE_SORT;
import static pl.rmakowiecki.eventhub.ui.screen_event_calendar.EventComparator.ascending;
import static pl.rmakowiecki.eventhub.ui.screen_event_calendar.EventComparator.getComparator;

/**
 * Created by m1per on 18.04.2017.
 */

class EventsFragmentPresenter extends BasePresenter<EventsFragmentView> {

    private RxLocationProvider provider = new RxLocationProvider();
    private EventsDistanceCalculator calculator = new EventsDistanceCalculator();
    private EventsRepository repository;
    private int position;
    private List<EventWDistance> eventsWithDistances = new ArrayList<>();
    private List<Event> allEvents = new ArrayList<>();

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
                .subscribe(this::update);
    }

    private void update(List<Event> events){
        boolean toRemove = false;
        int position = 0;
        if (allEvents.isEmpty()){
            allEvents = events;
        }else{
           for (Event event : events) {
               toRemove = false;
               for (Event currentEvent : allEvents){
                   if (currentEvent.getId().equals(event.getId())){
                       toRemove = true;
                       position = allEvents.indexOf(currentEvent);
                   }
               }
               if(toRemove){ allEvents.remove(position); }
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
    protected void onViewStarted(EventsFragmentView view) {
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
    public EventsFragmentView getNoOpView() {
        return NoOpEventsFragmentView.INSTANCE;
    }

    public void onEventAttendButtonClick() {

    }

    public void addEventParticipant(String eventId, int position) {
       repository.updateEventParticipants(eventId)
               .subscribe(new Action1<OperationStatus>() {
                   @Override
                   public void call(OperationStatus operationStatus) {
                       view.showActionStatus(operationStatus, position);
                   }
               });

    }
}
