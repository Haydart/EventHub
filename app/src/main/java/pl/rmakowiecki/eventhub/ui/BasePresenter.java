package pl.rmakowiecki.eventhub.ui;

public abstract class BasePresenter<V extends BaseView> {
    protected V view;

    void onViewStarted(V view) {
        this.view = view;
    }

    void onViewStopped() {
        view = getNoOpView();
    }

    public abstract V getNoOpView();
}
