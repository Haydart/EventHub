package pl.rmakowiecki.eventhub.api;

public abstract class RemoteServiceInteractor<I extends ResponseInterceptor> {
    protected I responseInterceptor;

    public void setResponseInterceptor(I responseInterceptor) {
        this.responseInterceptor = responseInterceptor;
    }

    public void removeResponseInterceptor() {
        responseInterceptor = null;
    }

    public void cancelSubscriptions() {
        //no-op
    }
}
