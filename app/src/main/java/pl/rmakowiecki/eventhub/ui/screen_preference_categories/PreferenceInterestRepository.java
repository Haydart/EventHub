package pl.rmakowiecki.eventhub.ui.screen_preference_categories;

import java.util.List;
import pl.rmakowiecki.eventhub.api.InterestsDatabaseInteractor;
import pl.rmakowiecki.eventhub.model.local.Interest;
import pl.rmakowiecki.eventhub.repository.AddOperationRepository;
import pl.rmakowiecki.eventhub.repository.GenericQueryStatus;
import pl.rmakowiecki.eventhub.repository.QueryList;
import pl.rmakowiecki.eventhub.repository.Specification;
import rx.Observable;

public class PreferenceInterestRepository implements AddOperationRepository<Interest, GenericQueryStatus>, QueryList<Interest> {
    @Override
    public Observable<GenericQueryStatus> add(Interest item) {
        // TODO: 2017-04-23  
        return Observable.empty();
    }

    @Override
    public Observable<GenericQueryStatus> add(Iterable<Interest> items) {
        // TODO: 2017-04-23  
        return Observable.empty();
    }

    @Override
    public Observable<List<Interest>> query(Specification specification) {
        return new InterestsDatabaseInteractor().getData();
    }
}
