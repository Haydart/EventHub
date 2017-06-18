package pl.rmakowiecki.eventhub.api;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import rx.Observable;
import rx.subjects.PublishSubject;

public abstract class BaseSpecificStorageInteractor<T> {
    protected FirebaseStorage firebaseStorage;
    protected StorageReference storageQueryNode;
    protected PublishSubject<T> publishSubject;

    public BaseSpecificStorageInteractor() {
        this.firebaseStorage = FirebaseStorage.getInstance();
    }

    protected abstract void setStorageQueryNode(String childKey);

    public abstract Observable<T> getData(String childKey);
}
