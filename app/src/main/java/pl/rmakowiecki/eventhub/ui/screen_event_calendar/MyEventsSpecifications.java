package pl.rmakowiecki.eventhub.ui.screen_event_calendar;

import pl.rmakowiecki.eventhub.ui.screen_events_map.EventsSpecification;

/**
 * Created by m1per on 30.04.2017.
 */

public class MyEventsSpecifications implements EventsSpecification {
    private int tabPosition;

    public MyEventsSpecifications(int position)
    {
        tabPosition = position;
    }

    public int getTabPosition() {
        return tabPosition;
    }
}
