package pl.rmakowiecki.eventhub.ui.start_screen;

import pl.rmakowiecki.eventhub.ui.BasePresenter;
import pl.rmakowiecki.eventhub.ui.preferences_screen.PreferencesRepository;
import pl.rmakowiecki.eventhub.ui.preferences_screen.PreferencesSpecification;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SplashPresenter extends BasePresenter<SplashView> {

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
        queryPreferences();
    }

    private void queryPreferences() {
        new PreferencesRepository()
                .query(new PreferencesSpecification() {})
                .compose(applySchedulers())
                .subscribe(view::savePreferences);
    }

    private <T> Observable.Transformer<T, T> applySchedulers() {
        return observable -> observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
