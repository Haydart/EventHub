package pl.rmakowiecki.eventhub.ui.screen_event_calendar;

import android.util.Log;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import java.util.Map;
import pl.rmakowiecki.eventhub.api.BaseDatabaseInteractor;
import pl.rmakowiecki.eventhub.model.local.Event;
import pl.rmakowiecki.eventhub.model.local.EventAttendee;
import pl.rmakowiecki.eventhub.model.local.User;
import pl.rmakowiecki.eventhub.model.remote.RemoteEvent;
import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by m1per on 17.04.2017.
 */

class EventsDatabaseInteractor extends BaseDatabaseInteractor<Event> {

    private static final String DATABASE_PATH = "app_data/events";

    private Event parseEventData(DataSnapshot dataSnapshot, int position) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Event event;
        String id;

        RemoteEvent remoteEvent = dataSnapshot.getValue(RemoteEvent.class);
        id = dataSnapshot.getKey();
        event = new Event(id, new RemoteEventMapper().map(remoteEvent));
        //TODO: more cases when filtering more advanced
        if (position == 1) {
            if (user != null) {
                event = filterForMyEvents(event, user.getUid());
            }
        }

        return event;
    }

    private Event filterForMyEvents(Event event, String userId) {
        for (EventAttendee attendee : event.getAttendees()) {
            if (attendee.getId().equals(userId)) {
                return event;
            }
        }

        return null;
    }

    @Override
    protected void setDatabaseQueryNode() {
        databaseQueryNode = firebaseDatabase
                .getReference(DATABASE_PATH);
    }

    @Override
    public Observable<Event> getData() {
        return Observable.empty();
    }

    public Observable<Event> getData(int position) {
        setDatabaseQueryNode();
        publishSubject = PublishSubject.create();
        databaseQueryNode.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                publishSubject.onNext(parseEventData(dataSnapshot, position));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                publishSubject.onNext(parseEventData(dataSnapshot, position));
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

        return publishSubject;
    }

    void addEvent(RemoteEvent item) {
        setDatabaseQueryNode();
        databaseQueryNode
                .push()
                .setValue(item)
                .addOnCompleteListener(task -> {
                    Log.d(getClass().getSimpleName(), "task successful? " + task.isSuccessful());
                });
    }
}
