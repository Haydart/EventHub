package pl.rmakowiecki.eventhub.api.auth;

import pl.rmakowiecki.eventhub.api.ResponseInterceptor;

public interface AuthResponseInterceptor extends ResponseInterceptor {
    void onInvalidCredentials();

    void onEmailAlreadyTaken();

    void onCredentialsDiscarded();
}
