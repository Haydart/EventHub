package pl.rmakowiecki.eventhub.ui.screen_event_calendar;

import android.util.Log;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import pl.rmakowiecki.eventhub.api.BaseDatabaseInteractor;
import pl.rmakowiecki.eventhub.model.local.Event;
import rx.Observable;
import rx.subjects.PublishSubject;

import static android.content.ContentValues.TAG;

/**
 * Created by m1per on 17.04.2017.
 */

public class EventsDatabaseInteractor extends BaseDatabaseInteractor<List<Event>> {

    private static final String DATABASE_PATH = "app_data/events";

    private List<Event> parseEventData(DataSnapshot dataSnapshot, int position) {

        List<Event> events = new ArrayList<>();
        Event event;
        Map<String, Boolean> usersMap;

        for (DataSnapshot child : dataSnapshot.getChildren()) {
            event = child.getValue(Event.class);
            events.add(event);
        }
        //TODO: more cases when filtering more advanced
        switch (position) {
            case 1:
                events = filterForMyEvents((ArrayList<Event>) events);
                break;
        }

        return events;
    }

    public ArrayList<Event> filterForMyEvents(ArrayList<Event> events) {
        ArrayList<Event> filteredList = new ArrayList<>();
        for (Event event : events) {

            if (event.getUsers().containsKey("abcd")) //should be imported from users database but there's none
            {
                filteredList.add(event);
            }
        }
        return filteredList;
    }

    @Override
    protected void setDatabaseQueryNode() {
        databaseQueryNode = firebaseDatabase
                .getReference(DATABASE_PATH);
    }

    @Override
    public Observable<List<Event>> getData() {
        return Observable.empty();
    }

    public Observable<List<Event>> getData(int position) {

        setDatabaseQueryNode();
        publishSubject = PublishSubject.create();
        databaseQueryNode.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                publishSubject.onNext(parseEventData(dataSnapshot, position));
                publishSubject.onCompleted();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "The read failed: " + databaseError.getCode());
            }
        });
        return publishSubject;
    }
}
