package pl.rmakowiecki.eventhub.api;

import com.google.firebase.auth.FirebaseAuth;
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
        databaseQueryNode = firebaseDatabase
                .getReference()
                .child(FirebaseConstants.USER_DATA_REFERENCE)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(FirebaseConstants.USER_NAME_REFERENCE);
    }

    @Override
    public Observable<String> getData() {
        setDatabaseQueryNode();
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

    public Observable<GenericQueryStatus> add(String name) {
        PublishSubject<GenericQueryStatus> publishSubject = PublishSubject.create();
        setDatabaseQueryNode();
        databaseQueryNode.setValue(name)
                .addOnSuccessListener(aVoid -> publishSubject.onNext(GenericQueryStatus.STATUS_SUCCESS))
                .addOnFailureListener(e -> publishSubject.onNext(GenericQueryStatus.STATUS_FAILURE));

        return publishSubject;
    }
}
