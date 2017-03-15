package pl.rmakowiecki.eventhub.ui.preferences_screen;

import java.util.List;

import pl.rmakowiecki.eventhub.model.local.Event;
import pl.rmakowiecki.eventhub.model.local.Preference;
import pl.rmakowiecki.eventhub.ui.BaseView;

interface PreferenceView extends BaseView {
    void showPreferences(List<Preference> preferenceList);
}