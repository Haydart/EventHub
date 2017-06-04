package pl.rmakowiecki.eventhub.ui.screen_preference_categories;

import java.util.List;
import pl.rmakowiecki.eventhub.api.LocaleDatabaseInteractor;
import pl.rmakowiecki.eventhub.model.local.PreferenceLocale;
import pl.rmakowiecki.eventhub.repository.QueryList;
import pl.rmakowiecki.eventhub.repository.Repository;
import pl.rmakowiecki.eventhub.repository.Specification;
import rx.Observable;

public class PreferencesLocaleRepository implements Repository<PreferenceLocale>, QueryList<PreferenceLocale> {
    @Override
    public void add(PreferenceLocale item) {
        // TODO: 2017-04-23
    }

    @Override
    public void add(Iterable<PreferenceLocale> items) {
        // TODO: 2017-04-23
    }

    @Override
    public void update(PreferenceLocale item) {
        // TODO: 2017-04-23
    }

    @Override
    public void remove(PreferenceLocale item) {
        // TODO: 2017-04-23
    }

    @Override
    public Observable<List<PreferenceLocale>> query(Specification specification) {
        return new LocaleDatabaseInteractor().getData();
    }
}
