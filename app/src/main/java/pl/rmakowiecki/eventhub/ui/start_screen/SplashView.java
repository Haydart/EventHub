package pl.rmakowiecki.eventhub.ui.start_screen;

import java.util.List;

import pl.rmakowiecki.eventhub.model.local.Preference;
import pl.rmakowiecki.eventhub.ui.BaseView;

interface SplashView extends BaseView {
    void savePreferences(List<Preference> preference);

    void checkIfFirstLaunch();

    void launchAppDelayed();
}
