package pl.rmakowiecki.eventhub.ui.screen_splash;

import android.util.Log;

import java.util.Locale;

import pl.rmakowiecki.eventhub.ui.BasePresenter;
import pl.rmakowiecki.eventhub.ui.screen_preference_categories.PreferenceInterestRepository;
import pl.rmakowiecki.eventhub.ui.screen_preference_categories.PreferenceInterestSpecification;
import pl.rmakowiecki.eventhub.ui.screen_preference_categories.PreferencesLocaleRepository;
import pl.rmakowiecki.eventhub.ui.screen_preference_categories.PreferencesLocaleSpecification;
import pl.rmakowiecki.eventhub.ui.screen_preference_categories.PreferencesRepository;
import pl.rmakowiecki.eventhub.ui.screen_preference_categories.PreferencesSpecification;
import pl.rmakowiecki.eventhub.ui.screen_user_profile.UserProfileImageRepository;
import pl.rmakowiecki.eventhub.util.LocaleUtils;

class SplashPresenter extends BasePresenter<SplashView> {

    private int requiredComponentCount;
    private int currentComponentCount;

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
        currentComponentCount = 0;
        requiredComponentCount = 3;
        view.checkIfFirstLaunch();
        queryPreferences();
        queryInterests();
        if (shouldLoadLocale()) {
            ++requiredComponentCount;
            queryLocales();
        }
        queryUserImage();
    }

    private boolean shouldLoadLocale() {
        return new LocaleUtils().hasLocale();
    }

    protected boolean canLaunchApplication() {
        Log.d("TAG", "Can launch? Loaded " + currentComponentCount + " components out of " + requiredComponentCount);
        return currentComponentCount >= requiredComponentCount;
    }

    protected void onComponentLoaded() {
        ++currentComponentCount;
        Log.d("TAG", "Loaded " + currentComponentCount + " components out of " + requiredComponentCount);
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

    private void queryLocales() {
        new PreferencesLocaleRepository()
                .query(new PreferencesLocaleSpecification() {})
                .compose(applySchedulers())
                .subscribe(view::saveLocales);
    }

    private void queryUserImage() {
        new UserProfileImageRepository()
                .querySingle(new PreferencesLocaleSpecification() {})
                .compose(applySchedulers())
                .subscribe(view::saveUserImage);
    }
}
