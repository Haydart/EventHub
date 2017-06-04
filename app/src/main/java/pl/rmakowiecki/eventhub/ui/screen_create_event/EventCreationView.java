package pl.rmakowiecki.eventhub.ui.screen_create_event;

import com.jenzz.noop.annotation.NoOp;
import pl.rmakowiecki.eventhub.ui.BaseView;

@NoOp
interface EventCreationView extends BaseView {
    void showDatePickerView();

    void showTimePickerView();

    void showEventPlaceAddress();
}
