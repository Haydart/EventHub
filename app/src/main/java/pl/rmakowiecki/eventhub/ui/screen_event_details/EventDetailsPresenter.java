package pl.rmakowiecki.eventhub.ui.screen_event_details;

import java.util.concurrent.TimeUnit;

import pl.rmakowiecki.eventhub.model.local.Event;
import pl.rmakowiecki.eventhub.repository.GenericQueryStatus;
import pl.rmakowiecki.eventhub.ui.BasePresenter;
import pl.rmakowiecki.eventhub.ui.screen_event_calendar.EventsRepository;
import pl.rmakowiecki.eventhub.util.BitmapUtils;
import pl.rmakowiecki.eventhub.util.UserAuthManager;
import pl.rmakowiecki.eventhub.util.UserManager;
import rx.Observable;

public class EventDetailsPresenter extends BasePresenter<EventDetailsView> {

    private EventImageRepository eventImageRepository;
    private UserManager userManager;
    private EventsRepository eventsRepository;
    private boolean isUserAttendingEvent;

    private static final int SHOW_BUTTON_RESULT_DELAY = 2000;
    private static final int BUTTON_ENABLE_DELAY = 4200;

    @Override
    public EventDetailsView getNoOpView() {
        return NoOpEventDetailsView.INSTANCE;
    }

    @Override
    protected void onViewStarted(EventDetailsView view) {
        super.onViewStarted(view);
        isUserAttendingEvent = false;
        userManager = new UserAuthManager();
        view.enableHomeButton();
        view.initEventDetails();
        eventImageRepository = new EventImageRepository();
        eventsRepository = new EventsRepository();
        loadEventPicture();
        handleAttendeesList();
    }

    private void loadEventPicture() {
        eventImageRepository
                .querySingle(new EventImageSpecification(view.getEventId()))
                .compose(applySchedulers())
                .subscribe(pictureData -> onEventPictureLoaded(pictureData));
    }

    private void handleAttendeesList() {
        if (userManager.isUserAuthorized())
            view.initAttendeesList();
        else
            view.hideAttendeesList();
    }

    private void onEventPictureLoaded(byte[] pictureData) {
        if (pictureData != null)
            view.displayEventPicture(BitmapUtils.getBitmapFromBytes(pictureData));
    }

    protected void onLoginButtonClicked() {
        view.launchAppFeaturesActivity();
    }

    public void onJoinButtonClicked(Event event, String displayName) {
        if (!userManager.isUserAuthorized())
            view.launchAppFeaturesActivity();
        else
            joinOrLeaveEvent(event, displayName);
     }

    private void joinOrLeaveEvent(Event event, String displayName) {
        view.showButtonProcessing();

        if (isUserAttendingEvent) {
            eventsRepository
                    .removeEventParticipant(event.getId())
                    .subscribe(this::onEventLeaveStatus);
        }
        else {
            eventsRepository
                    .updateEventParticipants(event.getId(), displayName)
                    .subscribe(this::onEventJoinStatus);
        }
    }

    private void onEventJoinStatus(GenericQueryStatus status) {
        if (status == GenericQueryStatus.STATUS_SUCCESS) {
            handleDelayedOperation(view::showSuccessMessage, SHOW_BUTTON_RESULT_DELAY);
            handleDelayedOperation(this::onUserAttendingEvent, BUTTON_ENABLE_DELAY);
        }
        else {
            handleDelayedOperation(view::showFailureMessage, SHOW_BUTTON_RESULT_DELAY);
        }
    }

    private void onEventLeaveStatus(GenericQueryStatus status) {
        if (status == GenericQueryStatus.STATUS_SUCCESS) {
            handleDelayedOperation(view::showSuccessMessage, SHOW_BUTTON_RESULT_DELAY);
            handleDelayedOperation(this::onUserLeavingEvent, BUTTON_ENABLE_DELAY);
        }
        else {
            handleDelayedOperation(view::showFailureMessage, SHOW_BUTTON_RESULT_DELAY);
        }
    }

    private void handleDelayedOperation(Runnable operation, int delay) {
        Observable.timer(delay, TimeUnit.MILLISECONDS)
                .compose(applySchedulers())
                .subscribe(ignored -> operation.run());
    }

    public void onUserAttendingEvent() {
        isUserAttendingEvent = true;
        updateJoinButtonState();
    }

    private void onUserLeavingEvent() {
        isUserAttendingEvent = false;
        updateJoinButtonState();
    }

    private void updateJoinButtonState() {
        view.updateJoinEventButton(isUserAttendingEvent);
    }
}
