package pl.rmakowiecki.eventhub.api;

import android.support.annotation.NonNull;
import android.util.Log;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
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

    public void add(String name) {
        setDatabaseQueryNode();
        databaseQueryNode.setValue(name)
                .addOnSuccessListener(aVoid -> {
                    Log.d(getClass().getSimpleName(), "User profile set successfully");
                    // TODO: 16/06/2017 propagate success to UI
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(getClass().getSimpleName(), "User profile setting failed");
                        // TODO: 16/06/2017 propagate failure to UI
                    }
                });
    }
}
