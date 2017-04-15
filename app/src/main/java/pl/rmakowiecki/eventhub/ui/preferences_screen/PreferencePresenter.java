package pl.rmakowiecki.eventhub.ui.preferences_screen;

import pl.rmakowiecki.eventhub.model.local.Preference;
import pl.rmakowiecki.eventhub.repository.Repository;
import pl.rmakowiecki.eventhub.ui.BasePresenter;

public class PreferencePresenter extends BasePresenter<PreferenceView> {

    private Repository<Preference> repository;

    PreferencePresenter() {
        repository = new PreferencesRepository();
    }

    private void onViewInitialization() {
        view.getPreferenceCategoryFromParcel();
    }

    @Override
    protected void onViewStarted(PreferenceView view) {
        super.onViewStarted(view);
        onViewInitialization();
    }

    protected void onPreferenceImageClick(PreferenceCategory category) {
        view.launchPreferenceDetailsScreen(category);
    }

    @Override
    public PreferenceView getNoOpView() {
        return null;
    }
}
