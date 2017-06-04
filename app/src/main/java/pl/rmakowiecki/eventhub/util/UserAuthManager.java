package pl.rmakowiecki.eventhub.util;

import com.google.firebase.auth.FirebaseAuth;

public class UserAuthManager implements UserProvider {
    @Override
    public boolean isUserAuthorized() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    @Override
    public void logoutUser() {
        FirebaseAuth.getInstance().signOut();
    }
}
