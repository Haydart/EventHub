package pl.rmakowiecki.eventhub.ui.screen_preference_categories;

import java.util.List;
import pl.rmakowiecki.eventhub.api.PreferencesDatabaseInteractor;
import pl.rmakowiecki.eventhub.model.local.RemotePreference;
import pl.rmakowiecki.eventhub.model.mappers.ModelMapper;
import pl.rmakowiecki.eventhub.model.mappers.RemotePreferenceModelMapper;
import pl.rmakowiecki.eventhub.repository.QueryList;
import pl.rmakowiecki.eventhub.repository.QueryStatus;
import pl.rmakowiecki.eventhub.repository.Repository;
import pl.rmakowiecki.eventhub.repository.Specification;
import rx.Observable;

public class PreferencesRepository implements Repository<PreferenceCategory, QueryStatus>, QueryList<PreferenceCategory> {

    private ModelMapper<RemotePreference, PreferenceCategory> modelMapper = new RemotePreferenceModelMapper();
    private PreferencesDatabaseInteractor preferencesDatabaseInteractor = new PreferencesDatabaseInteractor();

    @Override
    public Observable<QueryStatus> add(PreferenceCategory item) {
        return Observable.empty();
    }

    @Override
    public Observable<QueryStatus> add(Iterable<PreferenceCategory> items) {
        return Observable.empty();
    }

    @Override
    public void update(PreferenceCategory item) {
        //no-op
    }

    @Override
    public void remove(PreferenceCategory item) {
        //no-op
    }

    @Override
    public Observable<List<PreferenceCategory>> query(Specification specification) {
        return preferencesDatabaseInteractor.getData()
                .flatMap(Observable::from)
                .map(modelMapper::map)
                .toList();
    }
}
