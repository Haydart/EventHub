package pl.rmakowiecki.eventhub.api;

import com.google.firebase.storage.FirebaseStorage;
import pl.rmakowiecki.eventhub.repository.GenericQueryStatus;
import rx.Observable;
import rx.subjects.PublishSubject;

import static pl.rmakowiecki.eventhub.util.FirebaseConstants.USER_PROFILE_IMAGE_REFERENCE;

public class UserImageStorageInteractor extends BaseStorageInteractor<byte[]> {

    private final long FIVE_MEGABYTES = 5 * 1024 * 1024;

    @Override
    protected void setStorageQueryNode() {
        //no-op
    }

    @Override
    protected void setStorageQueryNode(String childKey) {
        storageQueryNode = FirebaseStorage.getInstance().getReference()
                .child(USER_PROFILE_IMAGE_REFERENCE)
                .child(childKey);
    }

    @Override
    public Observable<byte[]> getData() {
        return Observable.empty();
    }

    @Override
    public Observable<byte[]> getData(String childKey) {
        if (childKey.isEmpty()) {
            return Observable.empty();
        }
        setStorageQueryNode(childKey);
        publishSubject = PublishSubject.create();
        storageQueryNode
                .getBytes(FIVE_MEGABYTES)
                .addOnSuccessListener(bytes -> publishSubject.onNext(bytes));

        return publishSubject;
    }

    public Observable<GenericQueryStatus> add(String childKey, byte[] userImage) {
        PublishSubject<GenericQueryStatus> publishSubject = PublishSubject.create();
        setStorageQueryNode(childKey);
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
