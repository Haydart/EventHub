package pl.rmakowiecki.eventhub.ui.screen_event_calendar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import pl.rmakowiecki.eventhub.api.BaseDatabaseInteractor;
import pl.rmakowiecki.eventhub.repository.GenericQueryStatus;
import rx.Observable;
import rx.subjects.PublishSubject;

import static pl.rmakowiecki.eventhub.util.FirebaseConstants.EVENT_ATTENDEES_REFERENCE;

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

    Observable<GenericQueryStatus> addEventParticipant(String eventId) {
        publishSubject = PublishSubject.create();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        setDatabaseQueryNode();
        databaseQueryNode
                .child(eventId)
                .child(EVENT_ATTENDEES_REFERENCE)
                .child(user.getUid())
                .setValue("DISPLAY NAME")
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        publishSubject.onNext(GenericQueryStatus.STATUS_SUCCESS);
                    } else {
                        publishSubject.onNext(GenericQueryStatus.STATUS_FAILURE);
                    }
                    publishSubject.onCompleted();
                });
        return publishSubject;
    }
}