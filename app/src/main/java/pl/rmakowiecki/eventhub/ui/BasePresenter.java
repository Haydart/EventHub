package pl.rmakowiecki.eventhub.ui;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public abstract class BasePresenter<V extends BaseView> {
    protected V view;

    protected void onViewStarted(V view) {
        this.view = view;
    }

    protected void onViewStopped() {
        view = getNoOpView();
    }

    public abstract V getNoOpView();

    protected <T> Observable.Transformer<T, T> applySchedulers() {
        return observable -> observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
