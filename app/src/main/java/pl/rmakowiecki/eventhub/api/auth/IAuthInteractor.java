package pl.rmakowiecki.eventhub.api.auth;

import com.facebook.AccessToken;

public interface IAuthInteractor {
    void loginUserWithEmail(String email, String password);

    void registerUserWithEmail(String email, String password);

    void loginWithFacebook(AccessToken token);

    void loginWithGoogle();
}
