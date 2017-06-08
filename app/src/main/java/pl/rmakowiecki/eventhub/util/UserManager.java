package pl.rmakowiecki.eventhub.util;

public interface UserManager {
    boolean isUserAuthorized();

    void logoutUser();
}
