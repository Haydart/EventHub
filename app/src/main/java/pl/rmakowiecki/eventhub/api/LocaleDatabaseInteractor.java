package pl.rmakowiecki.eventhub.api;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.rmakowiecki.eventhub.model.local.PreferenceLocale;
import pl.rmakowiecki.eventhub.util.LocaleUtils;
import rx.Observable;
import rx.subjects.PublishSubject;

import static pl.rmakowiecki.eventhub.util.FirebaseConstants.APP_DATA_REFERENCE;
import static pl.rmakowiecki.eventhub.util.FirebaseConstants.CATEGORIES_LOCALE_REFERENCE;

@SuppressWarnings("unchecked")
public class LocaleDatabaseInteractor extends BaseDatabaseInteractor<List<PreferenceLocale>> {

    private LocaleUtils utils;
    @Override
    protected void setDatabaseQueryNode() {
        databaseQueryNode = firebaseDatabase
                .getReference(APP_DATA_REFERENCE)
                .child(CATEGORIES_LOCALE_REFERENCE)
                .child(utils.getLocaleString());
    }

    @Override
    protected void setDatabaseQueryNode(String childKey) {
        //no-op
    }

    private List<PreferenceLocale> parsePreferenceData(DataSnapshot dataSnapshot) {
        Map<String, Object> map = (HashMap<String, Object>)dataSnapshot.getValue();
        return getPreferenceListFromMap(map);
    }

    private List<PreferenceLocale> getPreferenceListFromMap(Map<String, Object> localeMap) {
        String localeString = utils.getLocaleString();
        List<PreferenceLocale> preferenceList = new ArrayList<>();
        for (Map.Entry<String, Object> entry : localeMap.entrySet()) {
            preferenceList.add(new PreferenceLocale(entry.getKey(), localeString, (Map<String, Object>) entry.getValue()));
        }

        return preferenceList;
    }

    @Override
    public Observable<List<PreferenceLocale>> getData() {
        utils = new LocaleUtils();
        if (utils.getLocaleString().isEmpty())
            return publishSubject;

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
                // TODO: 2017-05-18  
            }
        });

        return publishSubject;
    }

    @Override
    public Observable<List<PreferenceLocale>> getData(String childKey) {
        return Observable.empty();
    }
}
