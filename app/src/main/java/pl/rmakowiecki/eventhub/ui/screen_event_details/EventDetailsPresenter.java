package pl.rmakowiecki.eventhub.ui.screen_event_details;

import pl.rmakowiecki.eventhub.ui.BasePresenter;

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
    }
}
