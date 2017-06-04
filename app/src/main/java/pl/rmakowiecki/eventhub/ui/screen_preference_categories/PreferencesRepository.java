package pl.rmakowiecki.eventhub.ui.screen_preference_categories;

import java.util.List;
import pl.rmakowiecki.eventhub.api.PreferencesDatabaseInteractor;
import pl.rmakowiecki.eventhub.model.local.Preference;
import pl.rmakowiecki.eventhub.repository.QueryList;
import pl.rmakowiecki.eventhub.repository.Repository;
import pl.rmakowiecki.eventhub.repository.Specification;
import rx.Observable;

public class PreferencesRepository implements Repository<Preference>, QueryList<Preference> {

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
        return new PreferencesDatabaseInteractor().getData();
    }
}
