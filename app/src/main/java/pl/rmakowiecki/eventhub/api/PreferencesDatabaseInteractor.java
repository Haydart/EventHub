package pl.rmakowiecki.eventhub.api;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import pl.rmakowiecki.eventhub.model.local.Preference;
import rx.Observable;
import rx.subjects.PublishSubject;

import static pl.rmakowiecki.eventhub.util.FirebaseConstants.APP_DATA_REFERENCE;
import static pl.rmakowiecki.eventhub.util.FirebaseConstants.CATEGORIES_IMAGES_REFERENCE;
import static pl.rmakowiecki.eventhub.util.FirebaseConstants.CATEGORIES_REFERENCE;
import static pl.rmakowiecki.eventhub.util.FirebaseConstants.CATEGORIES_REFERENCES;
import static pl.rmakowiecki.eventhub.util.FirebaseConstants.EN_LOCALE_REFERENCE;
import static pl.rmakowiecki.eventhub.util.FirebaseConstants.PL_LOCALE_REFERENCE;

@SuppressWarnings("unchecked")
public class PreferencesDatabaseInteractor extends BaseDatabaseInteractor<List<Preference>> {

    @Override
    protected void setDatabaseQueryNode() {
        String locale = Locale.getDefault().getLanguage();
        if (!locale.equals(EN_LOCALE_REFERENCE) && !locale.equals(PL_LOCALE_REFERENCE))
            locale = EN_LOCALE_REFERENCE;

        databaseQueryNode = firebaseDatabase
                .getReference(APP_DATA_REFERENCE)
                .child(CATEGORIES_REFERENCE)
                .child(locale);
    }

    private List<Preference> parsePreferenceData(DataSnapshot dataSnapshot) {
        Map<String, Object> mainMap = (HashMap<String, Object>)dataSnapshot.getValue();
        Map<String, String> imagesMap = (HashMap<String, String>)mainMap.get(CATEGORIES_IMAGES_REFERENCE);
        Map<String, Object> categoryMap = (HashMap<String, Object>)mainMap.get(CATEGORIES_REFERENCES);
        
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
