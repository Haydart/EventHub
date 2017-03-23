package pl.rmakowiecki.eventhub.api;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import rx.Observable;
import rx.subjects.PublishSubject;

public abstract class BaseDatabaseInteractor<T> {
    protected FirebaseDatabase firebaseDatabase;
    protected DatabaseReference databaseQueryNode;
    protected PublishSubject<T> publishSubject;

    public BaseDatabaseInteractor() {
        this.firebaseDatabase = FirebaseDatabase.getInstance();
    }

    protected abstract void setDatabaseQueryNode();

    public abstract Observable<T> getData();
}
