package pl.rmakowiecki.eventhub.model.mappers;

import pl.rmakowiecki.eventhub.model.local.RemotePreference;
import pl.rmakowiecki.eventhub.ui.screen_preference_categories.PreferenceCategory;

public class RemotePreferenceModelMapper implements ModelMapper<RemotePreference, PreferenceCategory> {
    @Override
    public PreferenceCategory map(RemotePreference preference) {
        return new PreferenceCategory(preference.getName(), preference.getImageUrl(), preference.getSubCategories());
    }
}
