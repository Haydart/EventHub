package pl.rmakowiecki.eventhub.api.auth;

interface ResponseInterceptor {
    void onSuccess();

    void onNetworkConnectionError();
}
