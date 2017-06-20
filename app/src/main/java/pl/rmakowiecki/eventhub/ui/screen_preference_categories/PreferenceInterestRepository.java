package pl.rmakowiecki.eventhub.ui.screen_preference_categories;

import java.util.List;
import java.util.Map;

import pl.rmakowiecki.eventhub.api.InterestsDatabaseInteractor;
import pl.rmakowiecki.eventhub.model.local.Interest;
import pl.rmakowiecki.eventhub.repository.AddOperationRepository;
import pl.rmakowiecki.eventhub.repository.GenericQueryStatus;
import pl.rmakowiecki.eventhub.repository.QueryList;
import pl.rmakowiecki.eventhub.repository.Specification;
import rx.Observable;

public class PreferenceInterestRepository implements QueryList<Interest> {

    private InterestsDatabaseInteractor interactor = new InterestsDatabaseInteractor();

    @Override
    public Observable<List<Interest>> query(Specification specification) {
        return interactor.getData();
    }

    public void savePreferences(Map<String, List<String>> userDataFromSharedPreferences) {
        interactor.savePreferences(userDataFromSharedPreferences);
    }
}
