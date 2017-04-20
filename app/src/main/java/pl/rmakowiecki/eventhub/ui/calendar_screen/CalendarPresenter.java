package pl.rmakowiecki.eventhub.ui.calendar_screen;

import pl.rmakowiecki.eventhub.ui.BasePresenter;

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
        return null;
    }
}
