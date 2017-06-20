package pl.rmakowiecki.eventhub.util;

import android.support.annotation.NonNull;

public final class TextUtils {

    private TextUtils() {
    }

    public static boolean isEmpty(String string) {
        return string == null || string.trim().isEmpty();
    }

    public static boolean isNotEmpty(String string) {
        return !isEmpty(string.trim());
    }

    public static String getDefaultUsernameFromEmail(@NonNull String userEmail) {
        return userEmail.split("@")[0].trim();
    }
}
