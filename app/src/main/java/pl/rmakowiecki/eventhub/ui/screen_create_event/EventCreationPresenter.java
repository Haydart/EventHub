package pl.rmakowiecki.eventhub.ui.screen_create_event;

import pl.rmakowiecki.eventhub.ui.BasePresenter;

class EventCreationPresenter extends BasePresenter<EventCreationView> {

    @Override
    protected void onViewStarted(EventCreationView view) {
        super.onViewStarted(view);
        view.showEventPlaceAddress();
    }

    void onDatePickerButtonClicked() {
        view.showDatePickerView();
    }

    void onTimePickerButtonClicked() {
        view.showTimePickerView();
    }

    @Override
    public EventCreationView getNoOpView() {
        return NoOpEventCreationView.INSTANCE;
    }
}
