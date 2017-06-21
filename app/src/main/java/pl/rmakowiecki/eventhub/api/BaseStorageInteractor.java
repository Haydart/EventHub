package pl.rmakowiecki.eventhub.api;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import rx.Observable;
import rx.subjects.PublishSubject;

public abstract class BaseStorageInteractor<T> {
    protected FirebaseStorage firebaseStorage;
    protected StorageReference storageQueryNode;
    protected PublishSubject<T> publishSubject;

    public BaseStorageInteractor() {
        this.firebaseStorage = FirebaseStorage.getInstance();
    }

    protected abstract void setStorageQueryNode();
    protected abstract void setStorageQueryNode(String childKey);

    public abstract Observable<T> getData();
    public abstract Observable<T> getData(String childKey);
}
