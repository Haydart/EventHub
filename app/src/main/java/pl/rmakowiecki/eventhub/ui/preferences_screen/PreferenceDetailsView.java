package pl.rmakowiecki.eventhub.ui.preferences_screen;

import pl.rmakowiecki.eventhub.ui.BaseView;

interface PreferenceDetailsView extends BaseView {

    void getPreferenceCategoryFromParcel();
    void displayToolbarImage();
    void changeToolbarTitles();
    void loadAdapter();
    void enableHomeButton();
}
