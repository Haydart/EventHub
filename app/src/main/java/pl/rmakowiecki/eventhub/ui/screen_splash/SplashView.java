package pl.rmakowiecki.eventhub.ui.screen_splash;

import com.jenzz.noop.annotation.NoOp;
import java.util.List;
import pl.rmakowiecki.eventhub.model.local.Interest;
import pl.rmakowiecki.eventhub.model.local.Preference;
import pl.rmakowiecki.eventhub.model.local.PreferenceLocale;
import pl.rmakowiecki.eventhub.ui.BaseView;

@NoOp
interface SplashView extends BaseView {
    void savePreferences(List<Preference> preference);

    void saveInterests(List<Interest> interests);

    void saveLocales(List<PreferenceLocale> localesList);

    void saveUserImage(byte[] bytes);

    void checkIfFirstLaunch();

    void launchApplicationIfPossible();
}
