package pl.rmakowiecki.eventhub.ui.preferences_screen;

import java.util.List;
import pl.rmakowiecki.eventhub.model.local.Preference;
import pl.rmakowiecki.eventhub.ui.BaseView;

interface PreferenceView extends BaseView {
    void showPreferences(List<Preference> preference);

    void initPreferences(final List<PreferenceCategory> categories);

    void launchPreferenceDetailsScreen(PreferenceCategory category);
}
