package pl.rmakowiecki.eventhub.ui.screen_event_calendar;

import com.jenzz.noop.annotation.NoOp;
import pl.rmakowiecki.eventhub.ui.BaseView;
import pl.rmakowiecki.eventhub.util.SortTypes;

/**
 * Created by m1per on 09.04.2017.
 */
@NoOp
public interface CalendarView extends BaseView {
    void sortEvents(SortTypes type);
}
