package pl.rmakowiecki.eventhub.ui.start_screen;

import pl.rmakowiecki.eventhub.ui.BasePresenter;
import pl.rmakowiecki.eventhub.ui.preferences_screen.PreferencesRepository;
import pl.rmakowiecki.eventhub.ui.preferences_screen.PreferencesSpecification;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SplashPresenter extends BasePresenter<SplashView> {
    @Override
    public SplashView getNoOpView() {
        return null;
    }

    @Override
    protected void onViewStarted(SplashView view) {
        super.onViewStarted(view);
        onViewInitialization();
    }

    private void onViewInitialization() {
        view.checkIfFirstLaunch();
        view.launchAppDelayed();

        new PreferencesRepository()
                .query(new PreferencesSpecification() {})
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(view::savePreferences);
    }
}
