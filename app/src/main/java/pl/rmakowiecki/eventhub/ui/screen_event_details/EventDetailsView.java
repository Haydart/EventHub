package pl.rmakowiecki.eventhub.ui.screen_event_details;

import com.jenzz.noop.annotation.NoOp;
import pl.rmakowiecki.eventhub.ui.BaseView;

@NoOp
public interface EventDetailsView extends BaseView {
    void enableHomeButton();

    void initEventDetails();
}
