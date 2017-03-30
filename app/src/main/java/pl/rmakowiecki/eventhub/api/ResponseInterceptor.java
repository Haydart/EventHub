package pl.rmakowiecki.eventhub.api;

public interface ResponseInterceptor {
    void onSuccess();

    void onNetworkConnectionError();

    void onUnknownError();
}
