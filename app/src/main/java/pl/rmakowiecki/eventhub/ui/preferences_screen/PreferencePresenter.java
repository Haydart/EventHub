package pl.rmakowiecki.eventhub.ui.preferences_screen;

import pl.rmakowiecki.eventhub.api.PreferencesSpecification;
import pl.rmakowiecki.eventhub.model.local.Preference;
import pl.rmakowiecki.eventhub.repository.PreferencesRepository;
import pl.rmakowiecki.eventhub.repository.Repository;
import pl.rmakowiecki.eventhub.ui.BasePresenter;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PreferencePresenter extends BasePresenter<PreferenceView> {

    private Repository<Preference> repository;

    public PreferencePresenter() {
        repository = new PreferencesRepository();
    }

    public void onViewInitialization() {
        repository
                .query(new PreferencesSpecification() {})
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(view::showPreferences);
    }

    @Override
    protected void onViewStarted(PreferenceView view) {
        super.onViewStarted(view);
        onViewInitialization();
    }

    @Override
    public PreferenceView getNoOpView() {
        return null;
    }
}
