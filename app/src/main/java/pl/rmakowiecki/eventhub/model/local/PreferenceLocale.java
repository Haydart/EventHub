package pl.rmakowiecki.eventhub.model.local;

import java.util.Map;

public final class PreferenceLocale {
    private final String originalCategoryName;
    private final String localeString;
    private final Map<String, Object> localeNamesMap;

    public PreferenceLocale(String originalCategoryName, String localeString, Map<String, Object> localeNamesMap) {
        this.originalCategoryName = originalCategoryName;
        this.localeString = localeString;
        this.localeNamesMap = localeNamesMap;
    }

    public String getName() {
        return originalCategoryName;
    }
    public String getPreferenceLocaleString() {
        return localeString;
    }
    public Map<String, Object> getLocaleNamesMap() {
        return localeNamesMap;
    }
}
