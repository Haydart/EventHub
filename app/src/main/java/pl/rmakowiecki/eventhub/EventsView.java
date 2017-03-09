package pl.rmakowiecki.eventhub;

import java.util.List;
import pl.rmakowiecki.eventhub.model.local.Event;
import pl.rmakowiecki.eventhub.ui.BaseView;

interface EventsView extends BaseView {
    void showEvents(List<Event> eventsList);
}
