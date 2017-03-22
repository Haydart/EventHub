package pl.rmakowiecki.eventhub.api;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.rmakowiecki.eventhub.model.local.Preference;
import rx.subjects.PublishSubject;

public class PreferencesDatabaseInteractor extends BaseDatabaseInteractor {

    private PublishSubject<List<Preference>> publishSubject;

    @Override
    protected void setDatabaseQueryNode() {
        databaseQueryNode = firebaseDatabase.getInstance()
                .getReference("event_categories") // TODO: 22.03.2017 Add constant reference
                .child("en"); // TODO: 22.03.2017 Specify locale in specification
    }

    @Override
    public PublishSubject getPublishSubject() {
        setDatabaseQueryNode();
        publishSubject = PublishSubject.create();
        databaseQueryNode.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> categoryMap = (HashMap<String, Object>)dataSnapshot.getValue(); // "Category name" -> Category interest sub ArrayList
                List<Preference> preferenceList = new ArrayList<Preference>();
                int id = 1;
                for (Map.Entry<String, Object> entry : categoryMap.entrySet()) {
                    List<String> subCategories = new ArrayList<String>();
                    for (String interestName : (ArrayList<String>)entry.getValue())
                        subCategories.add(interestName);
                    preferenceList.add(new Preference(id++, entry.getKey(), subCategories));
                }

                publishSubject.onNext(preferenceList);
                publishSubject.onCompleted();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // TODO: 22.03.2017
            }
        });

        return publishSubject;
    }
}
