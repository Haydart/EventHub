package pl.rmakowiecki.eventhub.ui.screen_user_profile;

import pl.rmakowiecki.eventhub.repository.Specification;

public class UserProfileSpecification implements Specification {

    private final String userId;

    public UserProfileSpecification(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}
