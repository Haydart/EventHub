package pl.rmakowiecki.eventhub.api;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import pl.rmakowiecki.eventhub.model.local.Event;
import pl.rmakowiecki.eventhub.model.local.EventAttendee;
import pl.rmakowiecki.eventhub.model.mappers.RemoteEventMapper;
import pl.rmakowiecki.eventhub.model.remote.RemoteEvent;
import pl.rmakowiecki.eventhub.repository.QueryStatus;
import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by m1per on 17.04.2017.
 */

public class EventsDatabaseInteractor extends BaseDatabaseInteractor<Event> {

    private static final String DATABASE_PATH = "app_data/events";

    private Event parseEventData(DataSnapshot dataSnapshot, int position) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Event event;
        String id;

        RemoteEvent remoteEvent = dataSnapshot.getValue(RemoteEvent.class);
        id = dataSnapshot.getKey();
        event = new Event(new RemoteEventMapper().map(remoteEvent, id));
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
                //TODO: implement reaction for removing event
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                //no-op
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return publishSubject;
    }

    public Observable<QueryStatus> addEvent(RemoteEvent item) {
        PublishSubject<QueryStatus> publishSubject = PublishSubject.create();
        setDatabaseQueryNode();
        databaseQueryNode
                .push()
                .setValue(item)
                .addOnCompleteListener(task -> publishSubject.onNext(task.isSuccessful() ? QueryStatus.STATUS_SUCCESS : QueryStatus.STATUS_FAILURE));

        return publishSubject;
    }
}
