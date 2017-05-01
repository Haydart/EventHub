package pl.rmakowiecki.eventhub.ui.screen_app_features;

import com.jenzz.noop.annotation.NoOp;
import pl.rmakowiecki.eventhub.ui.BaseView;

@NoOp
interface AppFeaturesView extends BaseView {

    void changePageDescription(int position);

    void animateOutTransition(int screenTransitionDuration);

    void launchAuthScreen();

    void makeViewsVisible();
}
