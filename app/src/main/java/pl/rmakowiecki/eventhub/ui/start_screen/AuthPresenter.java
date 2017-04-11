package pl.rmakowiecki.eventhub.ui.start_screen;

import pl.rmakowiecki.eventhub.ui.BasePresenter;

class AuthPresenter extends BasePresenter<AuthView> {

    @Override
    public AuthView getNoOpView() {
        return null;
    }

    protected void onEventButtonClick() {
        view.launchEvents();
    }

    protected void onPreferencesButtonClick() {
        view.launchPreferences();
    }

}
