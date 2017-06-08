package pl.rmakowiecki.eventhub.util;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserAuthManager implements UserManager {
    @Override
    public boolean isUserAuthorized() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    @Override
    public void logoutUser() {
        FirebaseAuth.getInstance().signOut();
    }

    public String getCurrentUserId() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return currentUser != null ? currentUser.getUid() : "";
    }

    public String getUserDisplayedName() {
        // TODO: 05/06/2017 implement
        return "RemoteEvent Hub";
    }
}
