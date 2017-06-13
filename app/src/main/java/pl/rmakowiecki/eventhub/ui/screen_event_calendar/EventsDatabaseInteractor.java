package pl.rmakowiecki.eventhub.ui.screen_event_calendar;

import android.util.Log;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import pl.rmakowiecki.eventhub.api.BaseDatabaseInteractor;
import pl.rmakowiecki.eventhub.model.local.Event;
import pl.rmakowiecki.eventhub.model.local.User;
import pl.rmakowiecki.eventhub.model.remote.RemoteEvent;
import rx.Observable;
import rx.subjects.PublishSubject;

import static android.content.ContentValues.TAG;
import static pl.rmakowiecki.eventhub.util.FirebaseConstants.APP_DATA_REFERENCE;
import static pl.rmakowiecki.eventhub.util.FirebaseConstants.EVENTS_REFERENCE;
import static pl.rmakowiecki.eventhub.util.FirebaseConstants.EVENT_USERS_REFERENCE;

/**
 * Created by m1per on 17.04.2017.
 */

class EventsDatabaseInteractor extends BaseDatabaseInteractor<List<Event>> {

    private static final String DATABASE_PATH = "app_data/events";

    private List<Event> parseEventData(DataSnapshot dataSnapshot, int position) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        List<Event> events = new ArrayList<>();
        Event helperEvent, event;
        Map<String, Boolean> usersMap;
        String id;

        for (DataSnapshot child : dataSnapshot.getChildren()) {
            id = child.getKey();
            helperEvent = child.getValue(Event.class);
            event = new Event(id, helperEvent);
            events.add(event);
        }
        //TODO: more cases when filtering more advanced
        if (position == 1) {
            if (user != null) {
                events = filterForMyEvents((ArrayList<Event>) events, user.getUid());
            } else {
                events.clear();
            }
        }

        return events;
    }

    private List<Event> filterForMyEvents(ArrayList<Event> events, String userId) {
        ArrayList<Event> filteredList = new ArrayList<>();
        for (Event event : events) {

            // TODO: 05/06/2017 fix that hardcoded thing
            for (User user : event.getUsers()) {
                if (user.getId() == userId) {
                    filteredList.add((event));
                    break;
                }
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


        /*addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                publishSubject.onNext(parseEventData(dataSnapshot, position));
                publishSubject.onCompleted();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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
        });*/








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

    void addEventParticipant(String eventId) {
        updateParticipantsList(eventId);
    }

    void updateParticipantsList(String eventId) {
        HashMap<String, Boolean> users = new HashMap<>();
        FirebaseDatabase
                .getInstance()
                .getReference(APP_DATA_REFERENCE)
                .child(EVENTS_REFERENCE)
                .child(eventId)
                .child(EVENT_USERS_REFERENCE).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                saveParticipantsList((HashMap<String, Boolean>) dataSnapshot.getValue(), eventId);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "The read failed: " + databaseError.getCode());
            }
        });
    }

    void saveParticipantsList(HashMap<String, Boolean> users, String eventId) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();

        if (users.containsKey(userId)) {
            //TODO: handle "already participating case"
        } else {
            DatabaseReference eventParticipantsRef = FirebaseDatabase
                    .getInstance()
                    .getReference(APP_DATA_REFERENCE)
                    .child(EVENTS_REFERENCE)
                    .child(eventId)
                    .child(EVENT_USERS_REFERENCE);

            users.put(userId, true);
            eventParticipantsRef.setValue(users);
        }
    }
}
