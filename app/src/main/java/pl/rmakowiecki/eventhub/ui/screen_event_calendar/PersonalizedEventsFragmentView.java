package pl.rmakowiecki.eventhub.ui.screen_event_calendar;

import com.jenzz.noop.annotation.NoOp;
import java.util.List;
import pl.rmakowiecki.eventhub.model.local.EventWDistance;
import pl.rmakowiecki.eventhub.repository.GenericQueryStatus;
import pl.rmakowiecki.eventhub.ui.BaseView;

/**
 * Created by m1per on 17.06.2017.
 */
@NoOp
public interface PersonalizedEventsFragmentView extends BaseView {

    void showEvents(List<EventWDistance> eventsWithDistance, List<Boolean> attendance);

    void initEvents(List<EventWDistance> eventsWithDistance, List<Boolean> attendance);

    void showActionStatus(GenericQueryStatus genericQueryStatus);

    void showLeaveActionStatus(GenericQueryStatus operationStatus);
}
