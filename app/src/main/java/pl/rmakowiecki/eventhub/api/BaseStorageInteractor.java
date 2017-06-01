package pl.rmakowiecki.eventhub.api;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

    public abstract Observable<T> getData();
}
