package pl.rmakowiecki.eventhub.api;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import pl.rmakowiecki.eventhub.model.local.Interest;
import rx.Observable;
import rx.subjects.PublishSubject;

import static pl.rmakowiecki.eventhub.util.FirebaseConstants.EN_LOCALE_REFERENCE;
import static pl.rmakowiecki.eventhub.util.FirebaseConstants.PL_LOCALE_REFERENCE;
import static pl.rmakowiecki.eventhub.util.FirebaseConstants.USER_DATA_REFERENCE;
import static pl.rmakowiecki.eventhub.util.FirebaseConstants.USER_PREFERENCES_REFERENCE;

public class InterestsDatabaseInteractor extends BaseDatabaseInteractor<List<Interest>> {

    private FirebaseUser user;

    private boolean setUser() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        return user != null;
    }

    @Override
    protected void setDatabaseQueryNode() {
        databaseQueryNode = firebaseDatabase
                .getReference(USER_DATA_REFERENCE)
                .child(user.getUid())
                .child(USER_PREFERENCES_REFERENCE);
    }

    private List<Interest> parseInterestData(DataSnapshot dataSnapshot) {
        Map<String, List<Object>> interestsMap = (HashMap<String, List<Object>>)dataSnapshot.getValue();
        return getInterestListFromMap(interestsMap);
    }

    private List<Interest> getInterestListFromMap(Map<String, List<Object>> interestsMap) {
        List<Interest> interestsList = new ArrayList<>();

        if (interestsMap != null && !interestsMap.isEmpty()) {
            for (Map.Entry<String, List<Object>> entry : interestsMap.entrySet()) {
                List<String> subCategories = new ArrayList<>();
                for (Object subCategory : entry.getValue())
                    subCategories.add(subCategory.toString());
                interestsList.add(new Interest(entry.getKey(), subCategories));
            }
        }

        return interestsList;
    }

    @Override
    public Observable<List<Interest>> getData() {
        if (!setUser()) {
            return Observable.empty();
        }

        setDatabaseQueryNode();
        publishSubject = PublishSubject.create();
        databaseQueryNode.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                publishSubject.onNext(parseInterestData(dataSnapshot));
                publishSubject.onCompleted();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // TODO: 2017-04-23
            }
        });

        return publishSubject;
    }
}
