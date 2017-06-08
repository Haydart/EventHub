package pl.rmakowiecki.eventhub.ui.screen_preference_categories;

import pl.rmakowiecki.eventhub.model.local.RemotePreference;
import pl.rmakowiecki.eventhub.repository.ModelMapper;

public class RemotePreferenceModelMapper implements ModelMapper<RemotePreference, PreferenceCategory> {
    @Override
    public PreferenceCategory map(RemotePreference preference) {
        return new PreferenceCategory(preference.getName(), preference.getImageUrl(), preference.getSubCategories());
    }
}
