package pl.rmakowiecki.eventhub.api;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import java.util.ArrayList;
import java.util.List;
import pl.rmakowiecki.eventhub.model.local.Event;
import pl.rmakowiecki.eventhub.model.local.EventAttendee;
import pl.rmakowiecki.eventhub.model.mappers.RemoteEventMapper;
import pl.rmakowiecki.eventhub.model.remote.RemoteEvent;
import pl.rmakowiecki.eventhub.repository.GenericQueryStatus;
import pl.rmakowiecki.eventhub.ui.screen_preference_categories.PreferenceCategory;
import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by m1per on 17.04.2017.
 */

public class EventsDatabaseInteractor extends BaseDatabaseInteractor<Event> {

    private static final String DATABASE_PATH = "app_data/events";
    private static List<PreferenceCategory> interestsList;
    private String referenceKey;

    public EventsDatabaseInteractor() {
    }

    public EventsDatabaseInteractor(List<PreferenceCategory> interestsList) {
        this.interestsList = interestsList;
    }

    private Event parseEventData(DataSnapshot dataSnapshot, int position) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Event event;
        String id;
        if (dataSnapshot.getValue() == null) {
            return null;
        }
        RemoteEvent remoteEvent = dataSnapshot.getValue(RemoteEvent.class);
        id = dataSnapshot.getKey();
        event = new Event(new RemoteEventMapper().map(remoteEvent, id));
        if (position == 0) {
                event = filterForPersonalizedEvents(event);
        }
        if (position == 1) {
            if (user != null) {
                event = filterForMyEvents(event, user.getUid());
            } else {
                return null;
            }
        }

        return event;
    }

    private Event removeAttendeeFromLocalList(DataSnapshot dataSnapshot, int position) {
        Event event = parseEventData(dataSnapshot, position);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        List<EventAttendee> attendees = new ArrayList<>();
        if (event != null) {
            attendees = event.getAttendees();
        }
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

    private Event filterForPersonalizedEvents(Event event) {
        List<String> eventCategories = flattenCategoryList(event.getEventTags());
        List<String> userInterests = flattenCategoryList(interestsList);

        for (String eventCategory : eventCategories) {
            if (userInterests.contains(eventCategory)) {
                return event;
            }
        }
        return null;
    }

    private List<String> flattenCategoryList(List<PreferenceCategory> categoryList) {
        List<String> flatList = new ArrayList<>();
        for (PreferenceCategory category : categoryList) {
            flatList.addAll(category.getChildList());
        }
        return flatList;
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
    protected void setDatabaseQueryNode(String childKey) {
        //no-op
    }

    @Override
    public Observable<Event> getData() {
        return Observable.empty();
    }

    @Override
    public Observable<Event> getData(String childKey) {
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
                publishSubject.onNext(removeAttendeeFromLocalList(dataSnapshot, position));
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

        return publishSubject;
    }

    public Observable<Event> getData(int position, List<PreferenceCategory> displayList) {
        interestsList = displayList;
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
                publishSubject.onNext(removeAttendeeFromLocalList(dataSnapshot, position));
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

        return publishSubject;
    }

    public Observable<GenericQueryStatus> addEvent(RemoteEvent item) {
        PublishSubject<GenericQueryStatus> publishSubject = PublishSubject.create();
        setDatabaseQueryNode();
        DatabaseReference databaseReference = databaseQueryNode.push();
        referenceKey = databaseReference.getKey();
        databaseReference
                .setValue(item)
                .addOnCompleteListener(task -> publishSubject.onNext(task.isSuccessful() ? GenericQueryStatus.STATUS_SUCCESS : GenericQueryStatus.STATUS_FAILURE));

        return publishSubject;
    }

    public String getReferenceKey() {
        return referenceKey;
    }
}
