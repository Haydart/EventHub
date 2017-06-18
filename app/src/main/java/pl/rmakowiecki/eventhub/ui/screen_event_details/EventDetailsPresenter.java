package pl.rmakowiecki.eventhub.ui.screen_event_details;

import pl.rmakowiecki.eventhub.ui.BasePresenter;
import pl.rmakowiecki.eventhub.util.BitmapUtils;
import pl.rmakowiecki.eventhub.util.UserAuthManager;
import pl.rmakowiecki.eventhub.util.UserManager;

public class EventDetailsPresenter extends BasePresenter<EventDetailsView> {

    private EventImageRepository eventImageRepository;
    private UserManager userManager;

    @Override
    public EventDetailsView getNoOpView() {
        return NoOpEventDetailsView.INSTANCE;
    }

    @Override
    protected void onViewStarted(EventDetailsView view) {
        super.onViewStarted(view);
        userManager = new UserAuthManager();
        view.enableHomeButton();
        view.initEventDetails();
        eventImageRepository = new EventImageRepository();
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
}
