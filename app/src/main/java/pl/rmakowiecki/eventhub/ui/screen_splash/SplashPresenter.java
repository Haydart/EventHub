package pl.rmakowiecki.eventhub.ui.screen_splash;

import java.util.Locale;

import pl.rmakowiecki.eventhub.ui.BasePresenter;
import pl.rmakowiecki.eventhub.ui.screen_preference_categories.PreferenceInterestRepository;
import pl.rmakowiecki.eventhub.ui.screen_preference_categories.PreferenceInterestSpecification;
import pl.rmakowiecki.eventhub.ui.screen_preference_categories.PreferencesLocaleRepository;
import pl.rmakowiecki.eventhub.ui.screen_preference_categories.PreferencesLocaleSpecification;
import pl.rmakowiecki.eventhub.ui.screen_preference_categories.PreferencesRepository;
import pl.rmakowiecki.eventhub.ui.screen_preference_categories.PreferencesSpecification;
import pl.rmakowiecki.eventhub.util.LocaleUtils;

import static pl.rmakowiecki.eventhub.util.FirebaseConstants.EN_LOCALE_REFERENCE;
import static pl.rmakowiecki.eventhub.util.FirebaseConstants.PL_LOCALE_REFERENCE;

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
        if (shouldLoadLocale())
            queryLocales();
    }

    private boolean shouldLoadLocale() {
        return new LocaleUtils().hasLocale();
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
}
