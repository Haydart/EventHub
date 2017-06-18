package pl.rmakowiecki.eventhub.ui.screen_event_details;

import pl.rmakowiecki.eventhub.ui.BasePresenter;
import pl.rmakowiecki.eventhub.util.BitmapUtils;

public class EventDetailsPresenter extends BasePresenter<EventDetailsView> {

    private EventImageRepository eventImageRepository;

    @Override
    public EventDetailsView getNoOpView() {
        return NoOpEventDetailsView.INSTANCE;
    }

    @Override
    protected void onViewStarted(EventDetailsView view) {
        super.onViewStarted(view);
        view.enableHomeButton();
        view.initEventDetails();
        eventImageRepository = new EventImageRepository();
        loadEventPicture();
    }

    private void loadEventPicture() {
        eventImageRepository
                .querySingle(new EventImageSpecification(view.getEventId()))
                .compose(applySchedulers())
                .subscribe(pictureData -> onEventPictureLoaded(pictureData));
    }

    private void onEventPictureLoaded(byte[] pictureData) {
        if (pictureData != null)
            view.displayEventPicture(BitmapUtils.getBitmapFromBytes(pictureData));
    }
}
