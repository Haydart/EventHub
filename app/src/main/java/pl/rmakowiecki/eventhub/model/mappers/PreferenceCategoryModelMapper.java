package pl.rmakowiecki.eventhub.model.mappers;

import pl.rmakowiecki.eventhub.model.local.RemotePreference;
import pl.rmakowiecki.eventhub.ui.screen_preference_categories.PreferenceCategory;

public class PreferenceCategoryModelMapper implements ModelMapper<PreferenceCategory, RemotePreference> {

    @Override
    public RemotePreference map(PreferenceCategory model) {
        return new RemotePreference(-1, model.getTitle(), model.getChildList(), model.getImageResourceName());
    }
}
