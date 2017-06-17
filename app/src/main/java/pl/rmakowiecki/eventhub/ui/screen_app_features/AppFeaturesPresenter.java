package pl.rmakowiecki.eventhub.ui.screen_app_features;

import java.util.concurrent.TimeUnit;
import pl.rmakowiecki.eventhub.ui.BasePresenter;
import rx.Observable;

class AppFeaturesPresenter extends BasePresenter<AppFeaturesView> {

    private static final int SCREEN_TRANSITION_DURATION = 400;

    @Override
    protected void onViewVisible() {
        super.onViewVisible();
        view.makeViewsVisible();
        view.animateInTransition(SCREEN_TRANSITION_DURATION);
    }

    void onPageSelected(int position) {
        view.changePageDescription(position);
    }

    void onSignUpButtonClicked() {
        view.animateOutTransition(SCREEN_TRANSITION_DURATION);
        Observable.timer(SCREEN_TRANSITION_DURATION, TimeUnit.MILLISECONDS)
                .compose(applySchedulers())
                .subscribe(ignored -> view.launchAuthScreen());
    }

    @Override
    public AppFeaturesView getNoOpView() {
        return NoOpAppFeaturesView.INSTANCE;
    }
}
