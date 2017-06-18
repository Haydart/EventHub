package pl.rmakowiecki.eventhub.ui.screen_event_details;

import pl.rmakowiecki.eventhub.ui.BasePresenter;
import pl.rmakowiecki.eventhub.util.BitmapUtils;
import pl.rmakowiecki.eventhub.util.EventUtils;

public class EventDetailsPresenter extends BasePresenter<EventDetailsView> {
    @Override
    public EventDetailsView getNoOpView() {
        return NoOpEventDetailsView.INSTANCE;
    }

    @Override
    protected void onViewStarted(EventDetailsView view) {
        super.onViewStarted(view);
        view.enableHomeButton();
        view.initEventDetails();
        loadEventPicture();
    }

    private void loadEventPicture() {
        EventUtils
                .getEventPicture(view.getEventId())
                .compose(applySchedulers())
                .subscribe(pictureData -> onEventPictureLoaded(pictureData));
    }

    private void onEventPictureLoaded(byte[] pictureData) {
        if (pictureData != null)
            view.displayEventPicture(BitmapUtils.getBitmapFromBytes(pictureData));
    }
}
