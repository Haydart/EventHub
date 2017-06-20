package pl.rmakowiecki.eventhub.api.auth;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public interface AuthResponseInterceptor extends ResponseInterceptor {
    void onInvalidCredentials();

    void onEmailAlreadyTaken();

    void onCredentialsDiscarded();

    void onUnknownError();

    void onFacebookLoginSuccess();

    void onFacebookLoginError();

    void onGoogleLoginSuccess(GoogleSignInAccount signInAccount);

    void onGoogleLoginError();

    void onFirebaseGoogleLoginSuccess();
}
