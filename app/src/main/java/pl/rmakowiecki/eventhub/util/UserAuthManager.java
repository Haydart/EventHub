package pl.rmakowiecki.eventhub.util;

import android.content.Context;
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

    @Override
    public String getCurrentUserId() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return currentUser != null ? currentUser.getUid() : "";
    }

    @Override
    public String getUserDisplayedName(Context context) {
        return new PreferencesManager(context).getUserName();
    }

    @Override
    public String getUserEmail(Context context) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user != null ? user.getEmail() : "";
    }
}
