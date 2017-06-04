package pl.rmakowiecki.eventhub.ui.screen_create_event;

import java.util.Calendar;
import pl.rmakowiecki.eventhub.ui.BasePresenter;

class EventCreationPresenter extends BasePresenter<EventCreationView> {

    private Calendar eventTime = Calendar.getInstance();

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

    void onEventDatePicked(int year, int monthOfYear, int dayOfMonth) {
        eventTime.set(Calendar.YEAR, year);
    }

    void onEventTimePicked(int hourOfDay, int minute) {

    }

    @Override
    public EventCreationView getNoOpView() {
        return NoOpEventCreationView.INSTANCE;
    }
}
