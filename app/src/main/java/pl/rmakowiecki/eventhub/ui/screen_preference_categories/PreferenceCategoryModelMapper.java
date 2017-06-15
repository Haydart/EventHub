package pl.rmakowiecki.eventhub.ui.screen_preference_categories;

import pl.rmakowiecki.eventhub.model.local.RemotePreference;
import pl.rmakowiecki.eventhub.repository.ModelMapper;

public class PreferenceCategoryModelMapper implements ModelMapper<PreferenceCategory, RemotePreference> {

    @Override
    public RemotePreference map(PreferenceCategory model) {
        return new RemotePreference(-1, model.getTitle(), model.getChildList(), model.getImageResourceName());
    }
}
