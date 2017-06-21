package pl.rmakowiecki.eventhub.api.auth;

import pl.rmakowiecki.eventhub.ui.screen_auth.AuthPerspective;

interface ResponseInterceptor {
    void onSuccess(AuthPerspective authPerspective);

    void onNetworkConnectionError();
}
