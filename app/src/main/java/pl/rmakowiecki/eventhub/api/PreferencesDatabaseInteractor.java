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

@SuppressWarnings("unchecked")
public class PreferencesDatabaseInteractor extends BaseDatabaseInteractor<List<Preference>> {

    @Override
    protected void setDatabaseQueryNode() {
        databaseQueryNode = firebaseDatabase
                .getReference("app_data") // TODO: 2017-04-09 Add constant reference 
                .child("event_categories") // TODO: 22.03.2017 Add constant reference
                .child("en"); // TODO: 22.03.2017 Specify locale in specification
    }

    private List<Preference> parsePreferenceData(DataSnapshot dataSnapshot) {
        Map<String, Object> mainMap = (HashMap<String, Object>)dataSnapshot.getValue();
        Map<String, String> imagesMap = (HashMap<String, String>)mainMap.get("images"); // TODO: 2017-03-26 Add constant reference
        Map<String, Object> categoryMap = (HashMap<String, Object>)mainMap.get("categories"); // TODO: 2017-03-26 Add constant reference
        
        return getPreferenceListFromMap(imagesMap, categoryMap);
    }

    private List<Preference> getPreferenceListFromMap(Map<String, String> imagesMap, Map<String, Object> categoryMap) {
        List<Preference> preferenceList = new ArrayList<>();
        int id = 1;
        for (Map.Entry<String, Object> entry : categoryMap.entrySet()) {
            List<String> subCategories = new ArrayList<>();
            subCategories.addAll(((ArrayList<String>) entry.getValue()));

            String imageUrl = "";
            if (!imagesMap.isEmpty()) {
                String mapImageUrl = imagesMap.get(entry.getKey());
                if (mapImageUrl != null)
                    imageUrl = mapImageUrl;
            }

            preferenceList.add(new Preference(id++, entry.getKey(), subCategories, imageUrl));
        }

        return preferenceList;
    }

    @Override
    public Observable<List<Preference>> getData() {
        setDatabaseQueryNode();
        publishSubject = PublishSubject.create();
        databaseQueryNode.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                publishSubject.onNext(parsePreferenceData(dataSnapshot));
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
