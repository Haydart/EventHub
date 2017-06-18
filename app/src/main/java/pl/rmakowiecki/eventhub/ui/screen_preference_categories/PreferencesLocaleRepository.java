package pl.rmakowiecki.eventhub.ui.screen_preference_categories;

import java.util.List;
import pl.rmakowiecki.eventhub.api.LocaleDatabaseInteractor;
import pl.rmakowiecki.eventhub.model.local.PreferenceLocale;
import pl.rmakowiecki.eventhub.repository.AddOperationRepository;
import pl.rmakowiecki.eventhub.repository.GenericQueryStatus;
import pl.rmakowiecki.eventhub.repository.QueryList;
import pl.rmakowiecki.eventhub.repository.Specification;
import rx.Observable;

public class PreferencesLocaleRepository implements AddOperationRepository<PreferenceLocale, GenericQueryStatus>, QueryList<PreferenceLocale> {
    @Override
    public Observable<GenericQueryStatus> add(PreferenceLocale item) {
        return Observable.empty();
    }

    @Override
    public Observable<GenericQueryStatus> add(Iterable<PreferenceLocale> items) {
        return Observable.empty();
    }

    @Override
    public Observable<List<PreferenceLocale>> query(Specification specification) {
        return new LocaleDatabaseInteractor().getData();
    }
}
