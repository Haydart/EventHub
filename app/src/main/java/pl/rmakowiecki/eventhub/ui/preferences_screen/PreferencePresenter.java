package pl.rmakowiecki.eventhub.ui.preferences_screen;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import pl.rmakowiecki.eventhub.ui.BasePresenter;

public class PreferencePresenter extends BasePresenter<PreferenceView> {

    private final int SHOW_BUTTON_SUCCESS_DELAY = 2000;
    private final int LAUNCH_MAP_ACTIVITY_DELAY = 3500;

    private void onViewInitialization() {
        view.saveParcelData();
    }

    @Override
    protected void onViewStarted(PreferenceView view) {
        super.onViewStarted(view);
        onViewInitialization();
        view.enableHomeButton();
    }

    protected void onPreferenceImageClick(PreferenceCategory category) {
        view.launchPreferenceDetailsScreen(category);
    }

    @Override
    public PreferenceView getNoOpView() {
        return NoOpPreferenceView.INSTANCE;
    }

    public void onPreferenceSaveButtonClick() {
        view.savePreferences();
    }

    public void onPreferenceSave() {
        delayButtonAnimation();
        delayMapLaunch();
    }

    private void delayButtonAnimation() {
        Observable.just(null)
                .delay(SHOW_BUTTON_SUCCESS_DELAY, TimeUnit.MILLISECONDS)
                .compose(applySchedulers())
                .subscribe(ignored -> {
                    view.showButtonSuccess();
                });
    }

    private void delayMapLaunch() {
        Observable.just(null)
                .delay(LAUNCH_MAP_ACTIVITY_DELAY, TimeUnit.MILLISECONDS)
                .compose(applySchedulers())
                .subscribe(ignored -> {
                    view.launchMapAndFinish();
                });
    }

    private <T> Observable.Transformer<T, T> applySchedulers() {
        return observable -> observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
