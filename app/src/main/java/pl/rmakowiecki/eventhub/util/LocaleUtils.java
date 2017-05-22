package pl.rmakowiecki.eventhub.util;

import java.util.Locale;

import static pl.rmakowiecki.eventhub.util.FirebaseConstants.PL_LOCALE_REFERENCE;

public class LocaleUtils {
    public boolean hasLocale() {
        return !getLocaleString().isEmpty();
    }

    public String getLocaleString() {
        String locale = Locale.getDefault().getLanguage();
        if (!locale.equals(PL_LOCALE_REFERENCE))
            return "";

        return locale;
    }
}
