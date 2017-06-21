package pl.rmakowiecki.eventhub.api;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import pl.rmakowiecki.eventhub.repository.GenericQueryStatus;
import pl.rmakowiecki.eventhub.util.FirebaseConstants;
import rx.Observable;
import rx.subjects.PublishSubject;

public class UserProfileInteractor extends BaseDatabaseInteractor<String> {

    @Override
    protected void setDatabaseQueryNode() {
        //no-op
    }

    @Override
    protected void setDatabaseQueryNode(String userId) {
        databaseQueryNode = firebaseDatabase
                .getReference()
                .child(FirebaseConstants.USER_DATA_REFERENCE)
                .child(userId)
                .child(FirebaseConstants.USER_NAME_REFERENCE);
    }

    @Override
    public Observable<String> getData() {
        return Observable.empty();
    }

    @Override
    public Observable<String> getData(String userId) {
        setDatabaseQueryNode(userId);
        publishSubject = PublishSubject.create();
        databaseQueryNode.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                publishSubject.onNext((String) dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //no-op
            }
        });
        return publishSubject;
    }

    public Observable<GenericQueryStatus> add(String userId, String name) {
        PublishSubject<GenericQueryStatus> publishSubject = PublishSubject.create();
        setDatabaseQueryNode(userId);
        databaseQueryNode.setValue(name)
                .addOnSuccessListener(aVoid -> publishSubject.onNext(GenericQueryStatus.STATUS_SUCCESS))
                .addOnFailureListener(e -> publishSubject.onNext(GenericQueryStatus.STATUS_FAILURE));

        return publishSubject;
    }
}
