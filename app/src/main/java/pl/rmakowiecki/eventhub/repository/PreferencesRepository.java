package pl.rmakowiecki.eventhub.repository;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.rmakowiecki.eventhub.model.local.Preference;
import rx.Observable;
import rx.subjects.PublishSubject;

public class PreferencesRepository implements Repository<Preference> {

    private PublishSubject<List<Preference>> publishSubject;

    @Override
    public void add(Preference item) {
        // TODO: 22.03.2017  
    }

    @Override
    public void add(Iterable<Preference> items) {
        // TODO: 22.03.2017  
    }

    @Override
    public void update(Preference item) {
        // TODO: 2017-03-16
    }

    @Override
    public void remove(Preference item) {
        // TODO: 22.03.2017  
    }

    @Override
    public Observable<List<Preference>> query(Specification specification) {
        publishSubject = PublishSubject.create();
        FirebaseDatabase
                .getInstance()
                .getReference("event_categories") // TODO: 22.03.2017 Add constant reference
                .child("en") // TODO: 22.03.2017 Specify locale in specification
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String, Object> categoryMap = (HashMap<String, Object>)dataSnapshot.getValue(); // "Category name" -> Category interest sub ArrayList
                        List<Preference> preferenceList = new ArrayList<Preference>();
                        int id = 1;
                        for (Map.Entry<String, Object> entry : categoryMap.entrySet()) {
                            Preference preference = new Preference(id++, entry.getKey());
                            for (String interestName : (ArrayList<String>)entry.getValue())
                                preference.addSubcategory(interestName);
                            preferenceList.add(preference);
                        }

                        publishSubject.onNext(preferenceList);
                        publishSubject.onCompleted();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        return publishSubject;
    }
}
