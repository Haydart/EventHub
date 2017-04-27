package pl.rmakowiecki.eventhub.ui.screen_create_event;

import pl.rmakowiecki.eventhub.ui.BasePresenter;

class EventCreationPresenter extends BasePresenter<EventCreationView> {
    @Override
    public EventCreationView getNoOpView() {
        return NoOpEventCreationView.INSTANCE;
    }
}
