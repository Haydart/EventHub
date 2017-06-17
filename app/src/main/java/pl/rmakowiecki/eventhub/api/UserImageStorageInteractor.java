package pl.rmakowiecki.eventhub.api;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;

import pl.rmakowiecki.eventhub.repository.GenericQueryStatus;
import rx.Observable;
import rx.subjects.PublishSubject;

import static pl.rmakowiecki.eventhub.util.FirebaseConstants.USER_PROFILE_IMAGE_REFERENCE;

public class UserImageStorageInteractor extends BaseStorageInteractor<byte[]> {

    private final long FIVE_MEGABYTES = 5 * 1024 * 1024;
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
        storageQueryNode.getBytes(FIVE_MEGABYTES).addOnSuccessListener(bytes -> publishSubject.onNext(bytes));

        return publishSubject;
    }

    public Observable<GenericQueryStatus> add(byte[] userImage) {
        PublishSubject<GenericQueryStatus> publishSubject = PublishSubject.create();
        setUser();
        setStorageQueryNode();
        storageQueryNode.putBytes(userImage)
                .addOnSuccessListener(taskSnapshot -> {
                    publishSubject.onNext(GenericQueryStatus.STATUS_SUCCESS);
                })
                .addOnFailureListener(e -> {
                    publishSubject.onNext(GenericQueryStatus.STATUS_FAILURE);
                });

        return publishSubject;
    }
}
