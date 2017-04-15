package pl.rmakowiecki.eventhub.ui.preferences_screen;

import java.util.List;
import pl.rmakowiecki.eventhub.ui.BaseView;

interface PreferenceView extends BaseView {
    void getPreferenceCategoryFromParcel();

    void initPreferences(final List<PreferenceCategory> categories);

    void launchPreferenceDetailsScreen(PreferenceCategory category);
}
