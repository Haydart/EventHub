package pl.rmakowiecki.eventhub.ui.screen_event_calendar;

import pl.rmakowiecki.eventhub.ui.BasePresenter;
import pl.rmakowiecki.eventhub.util.SortTypes;

/**
 * Created by m1per on 09.04.2017.
 */

public class CalendarPresenter extends BasePresenter<CalendarView> {

    @Override
    protected void onViewStarted(CalendarView view) {
        super.onViewStarted(view);
    }

    @Override
    public CalendarView getNoOpView() {
        return NoOpCalendarView.INSTANCE;
    }

    public void onSortOptionSelected(SortTypes type) {
        view.sortEvents(type);
    }
}
