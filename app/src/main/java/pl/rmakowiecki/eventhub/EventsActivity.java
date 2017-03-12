package pl.rmakowiecki.eventhub;

import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import pl.rmakowiecki.eventhub.model.local.Event;
import pl.rmakowiecki.eventhub.ui.BaseActivity;

public class EventsActivity extends BaseActivity<EventsPresenter> implements EventsView {

    @BindView(R.id.helloworld_text_view)TextView helloWorldTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        helloWorldTextView.setText("Activity has started!");
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
