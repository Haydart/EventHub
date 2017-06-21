package pl.rmakowiecki.eventhub.ui.screen_event_calendar;

import com.jenzz.noop.annotation.NoOp;
import java.util.List;
import pl.rmakowiecki.eventhub.model.local.EventWDistance;
import pl.rmakowiecki.eventhub.repository.GenericQueryStatus;
import pl.rmakowiecki.eventhub.ui.BaseView;

/**
 * Created by m1per on 18.04.2017.
 */
@NoOp
public interface EventsFragmentView extends BaseView {

    void showEvents(List<EventWDistance> eventsWithDistance, List<Boolean> attendance);

    void initEvents(List<EventWDistance> eventsWithDistance, List<Boolean> attendance);

    void showActionStatus(GenericQueryStatus genericQueryStatus);
}
