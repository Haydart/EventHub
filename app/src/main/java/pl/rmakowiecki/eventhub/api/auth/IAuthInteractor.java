package pl.rmakowiecki.eventhub.api.auth;

import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public interface IAuthInteractor {
    void loginUserWithEmail(String email, String password);

    void registerUserWithEmail(String email, String password);

    void loginWithFacebook(AccessToken token);

    void loginWithGoogle(String tokenId, GoogleSignInAccount account);
}
