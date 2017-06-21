package pl.rmakowiecki.eventhub.ui.screen_splash;

import pl.rmakowiecki.eventhub.ui.BasePresenter;
import pl.rmakowiecki.eventhub.ui.screen_preference_categories.PreferenceInterestRepository;
import pl.rmakowiecki.eventhub.ui.screen_preference_categories.PreferenceInterestSpecification;
import pl.rmakowiecki.eventhub.ui.screen_preference_categories.PreferencesLocaleRepository;
import pl.rmakowiecki.eventhub.ui.screen_preference_categories.PreferencesLocaleSpecification;
import pl.rmakowiecki.eventhub.ui.screen_preference_categories.PreferencesRepository;
import pl.rmakowiecki.eventhub.ui.screen_preference_categories.PreferencesSpecification;
import pl.rmakowiecki.eventhub.ui.screen_user_profile.UserProfileRepository;
import pl.rmakowiecki.eventhub.util.LocaleUtils;

class SplashPresenter extends BasePresenter<SplashView> {

    private static final int REQUIRED_COMPONENT_COUNT = 1;
    private int currentComponentCount;

    @Override
    protected void onViewStarted(SplashView view) {
        super.onViewStarted(view);
        onViewInitialization();
    }

    private void onViewInitialization() {
        currentComponentCount = 0;
        view.checkIfFirstLaunch();
        queryInterests();
        if (shouldLoadLocale()) {
            queryLocales();
        }
        queryUserImage();
        queryPreferences();
    }

    private boolean shouldLoadLocale() {
        return new LocaleUtils().hasLocale();
    }

    private boolean canLaunchApplication() {
        return currentComponentCount >= REQUIRED_COMPONENT_COUNT;
    }

    @Override
    protected void onViewVisible() {
        super.onViewVisible();
        view.showSplashAnimation();
    }

    void onComponentLoaded() {
        ++currentComponentCount;
        if (canLaunchApplication()) {
            view.launchApplication();
        }
    }

    private void queryPreferences() {
        new PreferencesRepository()
                .query(new PreferencesSpecification() {})
                .compose(applySchedulers())
                .subscribe(preference -> view.savePreferences(preference));
    }

    private void queryInterests() {
        new PreferenceInterestRepository()
                .query(new PreferenceInterestSpecification() {})
                .compose(applySchedulers())
                .subscribe(view::saveInterests);
    }

    private void queryLocales() {
        new PreferencesLocaleRepository()
                .query(new PreferencesLocaleSpecification() {})
                .compose(applySchedulers())
                .subscribe(view::saveLocales);
    }

    private void queryUserImage() {
        new UserProfileRepository()
                .querySingle(new PreferencesLocaleSpecification() {})
                .compose(applySchedulers())
                .subscribe(view::saveUserData);
    }

    @Override
    public SplashView getNoOpView() {
        return NoOpSplashView.INSTANCE;
    }
}
