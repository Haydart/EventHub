package pl.rmakowiecki.eventhub.ui.screen_event_calendar;

import android.util.Log;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
import static pl.rmakowiecki.eventhub.ui.screen_event_calendar.EventComparator.descending;
import static pl.rmakowiecki.eventhub.ui.screen_event_calendar.EventComparator.getComparator;
import static pl.rmakowiecki.eventhub.util.FirebaseConstants.APP_DATA_REFERENCE;
import static pl.rmakowiecki.eventhub.util.FirebaseConstants.EVENTS_REFERENCE;

/**
 * Created by m1per on 18.04.2017.
 */

public class MyEventsFragmentPresenter extends BasePresenter<MyEventsFragmentView> {

    private RxLocationProvider provider = new RxLocationProvider();
    private EventsDistanceCalculator calculator = new EventsDistanceCalculator();
    private EventsRepository repository;
    private ArrayList<String> distances = new ArrayList<>();
    private int position;
    private List<EventWDistance> eventsWithDistances = new ArrayList<>();



    MyEventsFragmentPresenter(int position) {
        repository = new EventsRepository();
        this.position = position;
    }

    private void onViewInitialization() {
        acquireEvents();
        FirebaseDatabase
                .getInstance()
                .getReference(APP_DATA_REFERENCE)
                .child(EVENTS_REFERENCE).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("ONCHILDADDED", "CHILDADD");
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d("ONCHILDCHANGED", "CHILDCHANGE");
                acquireEvents();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
    protected void onViewStarted(MyEventsFragmentView view) {
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
    public MyEventsFragmentView getNoOpView() {
        return NoOpMyEventsFragmentView.INSTANCE;
    }
}
