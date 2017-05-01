package pl.rmakowiecki.eventhub.ui.screen_app_features;

import pl.rmakowiecki.eventhub.ui.BaseView;

interface AppFeaturesView extends BaseView {

    void changePageDescription(int position);

    void animateOutTransition(int screenTransitionDuration);

    void launchAuthScreen();

    void makeViewsVisible();
}
