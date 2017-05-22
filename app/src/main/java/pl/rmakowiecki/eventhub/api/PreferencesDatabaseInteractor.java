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

import static pl.rmakowiecki.eventhub.util.FirebaseConstants.APP_DATA_REFERENCE;
import static pl.rmakowiecki.eventhub.util.FirebaseConstants.CATEGORIES_REFERENCE;

@SuppressWarnings("unchecked")
public class PreferencesDatabaseInteractor extends BaseDatabaseInteractor<List<Preference>> {

    public static final String CATEGORY_IMAGE_NAME = "image";

    @Override
    protected void setDatabaseQueryNode() {
        databaseQueryNode = firebaseDatabase
                .getReference(APP_DATA_REFERENCE)
                .child(CATEGORIES_REFERENCE);
    }

    private List<Preference> parsePreferenceData(DataSnapshot dataSnapshot) {
        Map<String, Object> map = (HashMap<String, Object>)dataSnapshot.getValue();
        return getPreferenceListFromMap(map);
    }

    private List<Preference> getPreferenceListFromMap(Map<String, Object> categoryMap) {
        List<Preference> preferenceList = new ArrayList<>();
        int id = 1;
        for (Map.Entry<String, Object> entry : categoryMap.entrySet()) {
            String imageUrl = "";
            List<String> subCategories = new ArrayList<>();
            Map<String, Object> subcategoryMap = (Map<String, Object>) entry.getValue();
            for (Map.Entry<String, Object> subEntry : subcategoryMap.entrySet()) {
                String key = subEntry.getKey();
                String value = subEntry.getValue().toString();
                if (imageUrl.isEmpty() && key.equals(CATEGORY_IMAGE_NAME))
                    imageUrl = value;
                else
                    subCategories.add(value);
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
