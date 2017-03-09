package pl.rmakowiecki.eventhub;

import pl.rmakowiecki.eventhub.ui.BasePresenter;

class MainPresenter extends BasePresenter<MainView> {
    @Override
    public MainView getNoOpView() {
        return null;
    }
}
