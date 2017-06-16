package pl.rmakowiecki.eventhub.util;

import android.content.Context;

public interface UserManager {
    boolean isUserAuthorized();

    void logoutUser();

    String getCurrentUserId();

    String getUserDisplayedName(Context context);
}
