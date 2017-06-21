package pl.rmakowiecki.eventhub.ui.screen_event_calendar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import java.util.List;
import pl.rmakowiecki.eventhub.api.BaseDatabaseInteractor;
import pl.rmakowiecki.eventhub.model.local.Event;
import pl.rmakowiecki.eventhub.model.local.EventAttendee;
import pl.rmakowiecki.eventhub.model.mappers.RemoteEventMapper;
import pl.rmakowiecki.eventhub.model.remote.RemoteEvent;
import rx.Observable;
import rx.subjects.PublishSubject;

import static pl.rmakowiecki.eventhub.util.FirebaseConstants.EVENTS_REFERENCE;

/**
 * Created by m1per on 19.06.2017.
 */

public class UsersEventsDatabaseInteractor extends BaseDatabaseInteractor<Event> {

    private static final String DATABASE_PATH = "user_data";

    private Event parseEventData(DataSnapshot dataSnapshot) {
        Event event;
        String id;

        RemoteEvent remoteEvent = dataSnapshot.getValue(RemoteEvent.class);
        id = dataSnapshot.getKey();
        event = new Event(new RemoteEventMapper().map(remoteEvent, id));

        return event;
    }

    private Event removeAttendeeFromLocalList(DataSnapshot dataSnapshot) {
        Event event = parseEventData(dataSnapshot);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        List<EventAttendee> attendees = event.getAttendees();
        int attendeePosition = -1;
        for (EventAttendee attendee : attendees) {
            if (attendee.getId().equals(user.getUid())) {
                attendeePosition = attendees.indexOf(attendee);
            }
        }
        if (attendeePosition >= 0) {
            event.getAttendees().remove(attendeePosition);
        }
        return event;
    }

    @Override
    protected void setDatabaseQueryNode() {
        databaseQueryNode = firebaseDatabase
                .getReference(DATABASE_PATH);
    }

    @Override
    public Observable<Event> getData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        setDatabaseQueryNode();
        publishSubject = PublishSubject.create();
        if (user != null) {
            databaseQueryNode
                    .child(user.getUid())
                    .child(EVENTS_REFERENCE)
                    .addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            publishSubject.onNext(parseEventData(dataSnapshot));
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                            publishSubject.onNext(parseEventData(dataSnapshot));
                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {
                            publishSubject.onNext(removeAttendeeFromLocalList(dataSnapshot));
                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                            //no-op
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            //no-op
                        }
                    });
        }

        return publishSubject;
    }

    public Observable<Event> getData(int position) {
        return Observable.empty();
    }
}
