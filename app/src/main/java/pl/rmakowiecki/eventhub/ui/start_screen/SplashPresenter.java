package pl.rmakowiecki.eventhub.ui.start_screen;

import android.content.Intent;
import android.os.Handler;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import pl.rmakowiecki.eventhub.ui.BasePresenter;
import pl.rmakowiecki.eventhub.ui.events_map_screen.EventsMapActivity;
import pl.rmakowiecki.eventhub.ui.preferences_screen.PreferenceActivity;
import pl.rmakowiecki.eventhub.ui.preferences_screen.PreferencesRepository;
import pl.rmakowiecki.eventhub.ui.preferences_screen.PreferencesSpecification;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SplashPresenter extends BasePresenter<SplashView> {

    private final int LOADING_TIME = 5000; // TODO: 14.04.2017 Remove hardcoded value

    @Override
    public SplashView getNoOpView() {
        return NoOpSplashView.INSTANCE;
    }

    @Override
    protected void onViewStarted(SplashView view) {
        super.onViewStarted(view);
        onViewInitialization();
    }

    private void onViewInitialization() {
        view.checkIfFirstLaunch();
        launchAppDelayed();
        queryPreferences();
    }

    private void queryPreferences() {
        new PreferencesRepository()
                .query(new PreferencesSpecification() {})
                .compose(applySchedulers())
                .subscribe(view::savePreferences);
    }

    public void launchAppDelayed() {
        Observable.just(null)
                .delay(LOADING_TIME, TimeUnit.MILLISECONDS)
                .compose(applySchedulers())
                .subscribe(ignored -> {
                    view.launchApplication();
                });
    }

    private <T> Observable.Transformer<T, T> applySchedulers() {
        return observable -> observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
