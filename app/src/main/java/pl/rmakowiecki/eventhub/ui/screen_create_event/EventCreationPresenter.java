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
        eventTime.set(Calendar.MONTH, monthOfYear);
        eventTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
    }

    void onEventTimePicked(int hourOfDay, int minute) {
        eventTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        eventTime.set(Calendar.MINUTE, minute);
    }

    @Override
    public EventCreationView getNoOpView() {
        return NoOpEventCreationView.INSTANCE;
    }
}
