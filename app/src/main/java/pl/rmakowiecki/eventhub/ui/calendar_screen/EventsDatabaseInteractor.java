package pl.rmakowiecki.eventhub.ui.calendar_screen;

import android.util.Log;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import pl.rmakowiecki.eventhub.api.BaseDatabaseInteractor;
import pl.rmakowiecki.eventhub.model.local.Event;
import rx.Observable;
import rx.subjects.PublishSubject;

import static android.content.ContentValues.TAG;

/**
 * Created by m1per on 17.04.2017.
 */

public class EventsDatabaseInteractor extends BaseDatabaseInteractor<List<Event>> {

    private static final String DATABASE_PATH = "app_data/events";

    private List<Event> parseEventData(DataSnapshot dataSnapshot) {

        List<Event> events;

        GenericTypeIndicator<Map<String, Event>> t = new GenericTypeIndicator<Map<String, Event>>() {
        };
        Map<String, Event> map = dataSnapshot.getValue(t);
        events = new ArrayList<>(map.values());
        return events;
    }

    @Override
    protected void setDatabaseQueryNode() {
        databaseQueryNode = firebaseDatabase
                .getReference(DATABASE_PATH);
    }

    @Override
    public Observable<List<Event>> getData() {

        setDatabaseQueryNode();
        publishSubject = PublishSubject.create();
        databaseQueryNode.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                publishSubject.onNext(parseEventData(dataSnapshot));
                publishSubject.onCompleted();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG,"The read failed: " + databaseError.getCode());
            }
        });
        return publishSubject;
    }
}
