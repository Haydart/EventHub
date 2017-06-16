package pl.rmakowiecki.eventhub.api;

import android.util.Log;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import pl.rmakowiecki.eventhub.model.local.Event;
import pl.rmakowiecki.eventhub.model.mappers.RemoteEventMapper;
import pl.rmakowiecki.eventhub.model.remote.RemoteEvent;
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
        RemoteEvent event;

        for (DataSnapshot child : dataSnapshot.getChildren()) {
            event = child.getValue(RemoteEvent.class);
            events.add(new RemoteEventMapper().map(event));
        }
        //TODO: more cases when filtering more advanced
        switch (position) {
            case 1:
                events = filterForMyEvents((ArrayList<Event>) events);
                break;
        }
        return events;
    }

    private ArrayList<Event> filterForMyEvents(ArrayList<Event> events) {
        ArrayList<Event> filteredList = new ArrayList<>();
        for (Event event : events) {
            filteredList.add(event);
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

    public void addEvent(RemoteEvent item) {
        setDatabaseQueryNode();
        databaseQueryNode
                .push()
                .setValue(item)
                .addOnCompleteListener(task -> {
                    Log.d(getClass().getSimpleName(), "task successful? " + task.isSuccessful());
                });
    }
}