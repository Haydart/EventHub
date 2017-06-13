package pl.rmakowiecki.eventhub.ui.screen_preference_categories;

import java.util.List;
import pl.rmakowiecki.eventhub.api.PreferencesDatabaseInteractor;
import pl.rmakowiecki.eventhub.model.local.RemotePreference;
import pl.rmakowiecki.eventhub.repository.ModelMapper;
import pl.rmakowiecki.eventhub.repository.QueryList;
import pl.rmakowiecki.eventhub.repository.Repository;
import pl.rmakowiecki.eventhub.repository.Specification;
import rx.Observable;

public class PreferencesRepository implements Repository<PreferenceCategory>, QueryList<PreferenceCategory> {

    private ModelMapper<RemotePreference, PreferenceCategory> modelMapper = new RemotePreferenceModelMapper();
    private PreferencesDatabaseInteractor preferencesDatabaseInteractor = new PreferencesDatabaseInteractor();

    @Override
    public void add(PreferenceCategory item) {
        //no-op
    }

    @Override
    public void add(Iterable<PreferenceCategory> items) {
        //no-op
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
