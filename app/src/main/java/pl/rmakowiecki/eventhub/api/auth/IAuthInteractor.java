package pl.rmakowiecki.eventhub.api.auth;

import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import pl.rmakowiecki.eventhub.model.local.GoogleUser;

public interface IAuthInteractor {
    void loginUserWithEmail(String email, String password);

    void registerUserWithEmail(String email, String password);

    void loginWithFacebook(AccessToken token);

    void loginWithGoogle(String tokenId, GoogleUser user);
}
