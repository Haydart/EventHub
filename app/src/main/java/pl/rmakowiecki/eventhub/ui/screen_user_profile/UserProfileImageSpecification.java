package pl.rmakowiecki.eventhub.ui.screen_user_profile;

import pl.rmakowiecki.eventhub.repository.Specification;

public class UserProfileImageSpecification implements Specification {

    private String firebaseKey;

    public UserProfileImageSpecification(String key) {
        firebaseKey = key;
    }

    public String getFirebaseKey() {
        return firebaseKey;
    }
}
