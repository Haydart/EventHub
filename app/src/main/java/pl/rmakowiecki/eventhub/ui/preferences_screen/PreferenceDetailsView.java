package pl.rmakowiecki.eventhub.ui.preferences_screen;

import com.jenzz.noop.annotation.NoOp;

import pl.rmakowiecki.eventhub.ui.BaseView;

@NoOp
interface PreferenceDetailsView extends BaseView {

    void getPreferenceCategoryFromParcel();
    void displayToolbarImage();
    void changeToolbarTitles();
    void loadAdapter();
    void enableHomeButton();
}
