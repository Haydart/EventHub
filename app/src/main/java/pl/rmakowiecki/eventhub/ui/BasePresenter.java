package pl.rmakowiecki.eventhub.ui;

import java.util.ArrayList;
import java.util.List;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public abstract class BasePresenter<V extends BaseView> {
    protected V view;
    protected List<Subscription> subscriptions;

    public BasePresenter() {
        subscriptions = new ArrayList<>();
    }

    protected void onViewStarted(V view) {
        this.view = view;
    }

    protected void onViewStopped() {
        cancelCallbacks();
        view = getNoOpView();
    }

    protected void newSubscription(Subscription subscription) {
        subscriptions.add(subscription);
    }

    protected void cancelCallbacks() {
        for (Subscription subscription : subscriptions) {
            subscription.unsubscribe();
        }
    }

    public abstract V getNoOpView();

    protected <T> Observable.Transformer<T, T> applySchedulers() {
        return observable -> observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
