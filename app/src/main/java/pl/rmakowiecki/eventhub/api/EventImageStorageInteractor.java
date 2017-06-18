package pl.rmakowiecki.eventhub.api;

import com.google.firebase.storage.FirebaseStorage;

import pl.rmakowiecki.eventhub.repository.GenericQueryStatus;
import rx.Observable;
import rx.subjects.PublishSubject;

import static pl.rmakowiecki.eventhub.util.FirebaseConstants.EVENT_IMAGE_REFERENCE;

public class EventImageStorageInteractor extends BaseStorageInteractor<byte[]> {

    private final long FIVE_MEGABYTES = 5 * 1024 * 1024;
    private String firebaseKey;

    public EventImageStorageInteractor(String key) {
        firebaseKey = key;
    }

    @Override
    protected void setStorageQueryNode() {
        storageQueryNode = FirebaseStorage.getInstance().getReference()
                .child(EVENT_IMAGE_REFERENCE)
                .child(firebaseKey);
    }

    @Override
    public Observable<byte[]> getData() {
        setStorageQueryNode();
        publishSubject = PublishSubject.create();
        storageQueryNode
                .getBytes(FIVE_MEGABYTES)
                .addOnSuccessListener(bytes -> publishSubject.onNext(bytes));

        return publishSubject;
    }

    public Observable<GenericQueryStatus> add(byte[] eventImage) {
        PublishSubject<GenericQueryStatus> publishSubject = PublishSubject.create();
        setStorageQueryNode();
        storageQueryNode.putBytes(eventImage)
                .addOnSuccessListener(taskSnapshot -> {
                    publishSubject.onNext(GenericQueryStatus.STATUS_SUCCESS);
                })
                .addOnFailureListener(e -> {
                    publishSubject.onNext(GenericQueryStatus.STATUS_FAILURE);
                });

        return publishSubject;
    }
}
