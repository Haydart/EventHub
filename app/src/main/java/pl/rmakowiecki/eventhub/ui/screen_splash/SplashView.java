package pl.rmakowiecki.eventhub.ui.screen_splash;

import com.jenzz.noop.annotation.NoOp;
import java.util.List;
import pl.rmakowiecki.eventhub.model.local.Interest;
import pl.rmakowiecki.eventhub.model.local.PreferenceLocale;
import pl.rmakowiecki.eventhub.model.local.User;
import pl.rmakowiecki.eventhub.ui.BaseView;
import pl.rmakowiecki.eventhub.ui.screen_preference_categories.PreferenceCategory;

@NoOp
interface SplashView extends BaseView {
    void savePreferences(List<PreferenceCategory> preference);

    void saveInterests(List<Interest> interests);

    void saveLocales(List<PreferenceLocale> localesList);

    void saveUserData(User user);

    void checkIfFirstLaunch();

    void showSplashAnimation();

    void launchApplication();
}
