package pl.rmakowiecki.eventhub;

import android.os.Bundle;
import java.util.List;
import pl.rmakowiecki.eventhub.model.local.Event;
import pl.rmakowiecki.eventhub.ui.BaseActivity;

public class EventsActivity extends BaseActivity<EventsPresenter> implements EventsView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initPresenter() {
        presenter = new EventsPresenter();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    public void showEvents(List<Event> eventsList) {
        // stub implementation
    }
}
