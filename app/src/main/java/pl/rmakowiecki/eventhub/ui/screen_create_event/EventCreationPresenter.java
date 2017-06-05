package pl.rmakowiecki.eventhub.ui.screen_create_event;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import pl.rmakowiecki.eventhub.model.local.Event;
import pl.rmakowiecki.eventhub.model.local.LocationCoordinates;
import pl.rmakowiecki.eventhub.repository.Repository;
import pl.rmakowiecki.eventhub.ui.BasePresenter;
import pl.rmakowiecki.eventhub.ui.screen_event_calendar.EventsRepository;
import pl.rmakowiecki.eventhub.util.UserAuthManager;

class EventCreationPresenter extends BasePresenter<EventCreationView> {

    private Calendar eventTime = Calendar.getInstance();
    private Repository<Event> eventRepository = new EventsRepository();
    private UserAuthManager userAuthManager = new UserAuthManager();

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
        view.showPickedDate(String.format(Locale.getDefault(), "%s-%s-%s", dayOfMonth, monthOfYear, year));
    }

    void onEventTimePicked(int hourOfDay, int minute) {
        eventTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        eventTime.set(Calendar.MINUTE, minute);
        view.showPickedTime(String.format(Locale.getDefault(), "%d:%d", hourOfDay, minute));
    }

    void onEventCreationButtonClicked(LocationCoordinates eventCoordinates, String eventName, String eventAddress) {
        Map<String, Boolean> attendingUsersMap = new HashMap<>();
        attendingUsersMap.put(userAuthManager.getCurrentUserId(), true);
        Event event = new Event(
                "",
                eventName,
                eventTime.getTimeInMillis(),
                userAuthManager.getUserDisplayedName(),
                eventAddress,
                eventCoordinates.toString(),
                (HashMap<String, Boolean>) attendingUsersMap
        );
        eventRepository.add(event);
    }

    @Override
    public EventCreationView getNoOpView() {
        return NoOpEventCreationView.INSTANCE;
    }
}
