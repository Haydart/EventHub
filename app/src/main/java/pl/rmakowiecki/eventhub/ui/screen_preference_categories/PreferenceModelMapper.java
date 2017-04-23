package pl.rmakowiecki.eventhub.ui.screen_preference_categories;


import pl.rmakowiecki.eventhub.model.local.Preference;
import pl.rmakowiecki.eventhub.repository.ModelMapper;

public class PreferenceModelMapper implements ModelMapper<Preference, PreferenceCategory> {
    @Override
    public PreferenceCategory map(Preference preference) {
        return new PreferenceCategory(preference.getName(), preference.getImageUrl(), preference.getSubCategories());
    }
}
