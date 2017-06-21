package pl.rmakowiecki.eventhub.ui.screen_event_calendar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import pl.rmakowiecki.eventhub.api.BaseDatabaseInteractor;
import pl.rmakowiecki.eventhub.repository.GenericQueryStatus;
import rx.Observable;
import rx.subjects.PublishSubject;

import static pl.rmakowiecki.eventhub.util.FirebaseConstants.EVENTS_REFERENCE;
import static pl.rmakowiecki.eventhub.util.FirebaseConstants.EVENT_ATTENDEES_REFERENCE;
import static pl.rmakowiecki.eventhub.util.FirebaseConstants.USER_DATA_REFERENCE;

/**
 * Created by m1per on 13.06.2017.
 */

public class EventParticipantsDatabaseInteractor extends BaseDatabaseInteractor<GenericQueryStatus> {

    private static final String DATABASE_PATH = "app_data/events";

    @Override
    protected void setDatabaseQueryNode() {
        databaseQueryNode = firebaseDatabase
                .getReference(DATABASE_PATH);
    }

    @Override
    public Observable<GenericQueryStatus> getData() {
        return Observable.empty();
    }

    public Observable<GenericQueryStatus> addEventParticipant(String eventId) {
        publishSubject = PublishSubject.create();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        setDatabaseQueryNode();
        databaseQueryNode
                .child(eventId)
                .child(EVENT_ATTENDEES_REFERENCE)
                .child(user.getUid())
                .setValue("DISPLAY NAME") //TODO: user.getDisplayName when ready
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        publishSubject.onNext(GenericQueryStatus.STATUS_SUCCESS);
                    } else {
                        publishSubject.onNext(GenericQueryStatus.STATUS_FAILURE);
                    }
                    publishSubject.onCompleted();
                });

        copyEventToUser(eventId, user.getUid());
        return publishSubject;
    }

    private void copyEventToUser(String eventId, String userId) {
        DatabaseReference userEventsRef = FirebaseDatabase
                .getInstance()
                .getReference(USER_DATA_REFERENCE)
                .child(userId)
                .child(EVENTS_REFERENCE)
                .child(eventId);

        DatabaseReference eventRef = databaseQueryNode
                .child(eventId);

        eventRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userEventsRef.setValue(dataSnapshot.getValue(), (databaseError, databaseReference) -> {
                    //TODO: handle error
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //TODO handle error
            }
        });
    }

    public Observable<GenericQueryStatus> removeUserFromEvent(String eventId) {
        publishSubject = PublishSubject.create();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        setDatabaseQueryNode();
        databaseQueryNode
                .child(eventId)
                .child(EVENT_ATTENDEES_REFERENCE)
                .child(user.getUid())
                .removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        publishSubject.onNext(GenericQueryStatus.STATUS_SUCCESS);
                    } else {
                        publishSubject.onNext(GenericQueryStatus.STATUS_FAILURE);
                    }
                    publishSubject.onCompleted();
                });

        DatabaseReference userEventsRef = FirebaseDatabase
                .getInstance()
                .getReference(USER_DATA_REFERENCE)
                .child(user.getUid())
                .child(EVENTS_REFERENCE)
                .child(eventId);

        userEventsRef.removeValue();

        return publishSubject;
    }
}