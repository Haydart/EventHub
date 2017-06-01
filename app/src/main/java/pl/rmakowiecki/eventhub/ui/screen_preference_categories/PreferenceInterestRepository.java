package pl.rmakowiecki.eventhub.ui.screen_preference_categories;

import java.util.List;
import pl.rmakowiecki.eventhub.api.InterestsDatabaseInteractor;
import pl.rmakowiecki.eventhub.model.local.Interest;
import pl.rmakowiecki.eventhub.repository.Repository;
import pl.rmakowiecki.eventhub.repository.Specification;
import rx.Observable;

public class PreferenceInterestRepository implements Repository<Interest> {
    @Override
    public void add(Interest item) {
        // TODO: 2017-04-23  
    }

    @Override
    public void add(Iterable<Interest> items) {
        // TODO: 2017-04-23  
    }

    @Override
    public void update(Interest item) {
        // TODO: 2017-04-23  
    }

    @Override
    public void remove(Interest item) {
        // TODO: 2017-04-23
    }

    @Override
    public Observable<List<Interest>> query(Specification specification) {
        return new InterestsDatabaseInteractor().getData();
    }

    @Override
    public Observable<Interest> querySingle(Specification specification) {
        return Observable.empty();
    }
}
