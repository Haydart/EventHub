package pl.rmakowiecki.eventhub.ui.calendar_screen;

import com.jenzz.noop.annotation.NoOp;
import java.util.List;
import pl.rmakowiecki.eventhub.model.local.Event;
import pl.rmakowiecki.eventhub.ui.BaseView;

/**
 * Created by m1per on 18.04.2017.
 */
@NoOp
public interface EventsFragmentView extends BaseView {

    void showEvents(List<Event> preference);

    void initEvents(final List<Event> events);
}
