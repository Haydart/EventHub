package pl.rmakowiecki.eventhub.api.auth;

public interface AuthResponseInterceptor extends ResponseInterceptor {
    void onInvalidCredentials();

    void onEmailAlreadyTaken();

    void onCredentialsDiscarded();

    void onUnknownError();

    void onFacebookLoginSuccess();

    void onFacebookLoginError();

    void onGoogleLoginSuccess();

    void onGoogleLoginError();
}
