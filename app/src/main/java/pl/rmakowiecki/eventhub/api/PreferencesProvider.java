package pl.rmakowiecki.eventhub.api;

import java.util.List;
import pl.rmakowiecki.eventhub.model.local.Preference;

public class PreferencesProvider implements DataProvider<Preference> {

    @Override
    public Preference fetch() {
        return null;
    }

    @Override
    public List<Preference> fetchList() {
        return null;
    }
}
