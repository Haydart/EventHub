package pl.rmakowiecki.eventhub.api.auth;

import pl.rmakowiecki.eventhub.model.local.GoogleUser;

public interface AuthResponseInterceptor extends ResponseInterceptor {
    void onInvalidCredentials();

    void onEmailAlreadyTaken();

    void onCredentialsDiscarded();

    void onUnknownError();

    void onFacebookLoginSuccess();

    void onFacebookLoginError();

    void onGoogleLoginSuccess(String userIdToken, GoogleUser user);

    void onGoogleLoginError();

    void onDatabaseGoogleLoginSuccess(GoogleUser user);
}
