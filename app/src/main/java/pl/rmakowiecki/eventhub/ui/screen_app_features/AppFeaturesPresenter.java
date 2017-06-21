package pl.rmakowiecki.eventhub.ui.screen_app_features;

import java.util.concurrent.TimeUnit;
import pl.rmakowiecki.eventhub.ui.BasePresenter;
import rx.Observable;

class AppFeaturesPresenter extends BasePresenter<AppFeaturesView> {

    private static final int SCREEN_TRANSITION_DURATION = 400;
    private static final int LAST_TAB_POSITION = 3;
    private int tabPosition;

    @Override
    protected void onViewVisible() {
        super.onViewVisible();
        view.makeViewsVisible();
        view.animateInTransition(SCREEN_TRANSITION_DURATION);
    }

    void onPageSelected(int position) {
        tabPosition = position;
        view.changePageDescription(position);
        if (tabPosition != LAST_TAB_POSITION) {
            view.setButtonActionNextTab();
        } else {
            view.setButtonTextJoin();
        }
    }

    void onSignUpButtonClicked() {
        if (tabPosition != LAST_TAB_POSITION) {
            view.showNextFeatureTab();
        } else {
            view.animateOutTransition(SCREEN_TRANSITION_DURATION);
            Observable.timer(SCREEN_TRANSITION_DURATION, TimeUnit.MILLISECONDS)
                    .compose(applySchedulers())
                    .subscribe(ignored -> view.launchAuthScreen());
        }
    }

    @Override
    public AppFeaturesView getNoOpView() {
        return NoOpAppFeaturesView.INSTANCE;
    }
}
