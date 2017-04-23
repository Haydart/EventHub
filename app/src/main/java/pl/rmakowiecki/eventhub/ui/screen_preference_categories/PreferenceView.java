package pl.rmakowiecki.eventhub.ui.screen_preference_categories;

import com.jenzz.noop.annotation.NoOp;
import java.util.List;
import pl.rmakowiecki.eventhub.ui.BaseView;

@NoOp
interface PreferenceView extends BaseView {
    void saveParcelData();

    void initPreferences(final List<PreferenceCategory> categories);

    void launchPreferenceDetailsScreen(PreferenceCategory category);

    void savePreferences();

    void enableHomeButton();

    void showPreferencesSavingSuccess();

    void launchMapAndFinish();

    void showNotEnoughPreferencesMessage();
}
