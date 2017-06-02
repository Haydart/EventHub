package pl.rmakowiecki.eventhub.api;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;

import rx.Observable;
import rx.subjects.PublishSubject;

import static pl.rmakowiecki.eventhub.util.FirebaseConstants.USER_PROFILE_IMAGE_REFERENCE;

public class UserImageStorageInteractor extends BaseStorageInteractor<byte[]> {

    private FirebaseUser user;

    private boolean setUser() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        return user != null;
    }

    @Override
    protected void setStorageQueryNode() {
        storageQueryNode = FirebaseStorage.getInstance().getReference()
                .child(USER_PROFILE_IMAGE_REFERENCE)
                .child(user.getUid());
    }

    @Override
    public Observable<byte[]> getData() {
        if (!setUser()) {
            return Observable.empty();
        }

        setStorageQueryNode();
        publishSubject = PublishSubject.create();
        final long FIVE_MEGABYTES = 5 * 1024 * 1024;
        storageQueryNode.getBytes(FIVE_MEGABYTES).addOnSuccessListener(bytes -> publishSubject.onNext(bytes));

        return publishSubject;
    }
}