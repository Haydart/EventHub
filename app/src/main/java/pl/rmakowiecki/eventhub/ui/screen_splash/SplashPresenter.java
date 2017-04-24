package pl.rmakowiecki.eventhub.ui.screen_splash;

import pl.rmakowiecki.eventhub.ui.BasePresenter;
import pl.rmakowiecki.eventhub.ui.preferences_screen.PreferenceInterestRepository;
import pl.rmakowiecki.eventhub.ui.preferences_screen.PreferenceInterestSpecification;
import pl.rmakowiecki.eventhub.ui.screen_preference_categories.PreferencesRepository;
import pl.rmakowiecki.eventhub.ui.screen_preference_categories.PreferencesSpecification;

class SplashPresenter extends BasePresenter<SplashView> {

    @Override
    public SplashView getNoOpView() {
        return NoOpSplashView.INSTANCE;
    }

    @Override
    protected void onViewStarted(SplashView view) {
        super.onViewStarted(view);
        onViewInitialization();
    }

    private void onViewInitialization() {
        view.checkIfFirstLaunch();
        queryPreferences();
        queryInterests();
    }

    private void queryPreferences() {
        new PreferencesRepository()
                .query(new PreferencesSpecification() {})
                .compose(applySchedulers())
                .subscribe(view::savePreferences);
    }

    private void queryInterests() {
        new PreferenceInterestRepository()
                .query(new PreferenceInterestSpecification() {})
                .compose(applySchedulers())
                .subscribe(view::saveInterests);
    }
}