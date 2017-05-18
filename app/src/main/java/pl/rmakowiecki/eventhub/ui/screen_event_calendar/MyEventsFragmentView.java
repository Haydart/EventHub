package pl.rmakowiecki.eventhub.ui.screen_event_calendar;

import com.jenzz.noop.annotation.NoOp;
import java.util.List;
import pl.rmakowiecki.eventhub.model.local.Event;
import pl.rmakowiecki.eventhub.ui.BaseView;

/**
 * Created by m1per on 18.04.2017.
 */
@NoOp
public interface MyEventsFragmentView extends BaseView {

    void showEvents(List<Event> events, List<String> distances);

    void initEvents(final List<Event> events, List<String> distances);
}
