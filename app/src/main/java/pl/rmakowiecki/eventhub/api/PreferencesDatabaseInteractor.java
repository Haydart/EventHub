package pl.rmakowiecki.eventhub.api;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.rmakowiecki.eventhub.model.local.Preference;
import rx.Observable;
import rx.subjects.PublishSubject;

public class PreferencesDatabaseInteractor extends BaseDatabaseInteractor<List<Preference>> {

    @Override
    protected void setDatabaseQueryNode() {
        databaseQueryNode = firebaseDatabase.getInstance()
                .getReference("event_categories") // TODO: 22.03.2017 Add constant reference
                .child("en"); // TODO: 22.03.2017 Specify locale in specification
    }

    @Override
    public Observable<List<Preference>> getData() {
        setDatabaseQueryNode();
        publishSubject = PublishSubject.create();
        databaseQueryNode.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> mainMap = (HashMap<String, Object>)dataSnapshot.getValue();
                Map<String, String> imagesMap = (HashMap<String, String>)mainMap.get("images");
                Map<String, Object> categoryMap = (HashMap<String, Object>)mainMap.get("categories"); // "Category name" -> Category interest sub ArrayList
                List<Preference> preferenceList = new ArrayList<Preference>();
                int id = 1;
                for (Map.Entry<String, Object> entry : categoryMap.entrySet()) {
                    List<String> subCategories = new ArrayList<String>();
                    for (String interestName : (ArrayList<String>)entry.getValue())
                        subCategories.add(interestName);

                    String imageURL = "";
                    if (!imagesMap.isEmpty()) {
                        String s = imagesMap.get(entry.getKey());
                        if (s != null)
                            imageURL = s;
                    }

                    preferenceList.add(new Preference(id++, entry.getKey(), subCategories, imageURL));
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
