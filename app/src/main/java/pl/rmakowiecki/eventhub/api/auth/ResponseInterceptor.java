package pl.rmakowiecki.eventhub.api.auth;

interface ResponseInterceptor {
    void onSuccess(boolean register);

    void onNetworkConnectionError();
}
