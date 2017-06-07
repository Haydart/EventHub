package pl.rmakowiecki.eventhub.ui.screen_event_details;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import pl.rmakowiecki.eventhub.R;
import pl.rmakowiecki.eventhub.model.local.UserProfile;
import pl.rmakowiecki.eventhub.ui.BaseActivity;

public class EventDetailsActivity extends BaseActivity<EventDetailsPresenter> implements EventDetailsView {

    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @BindView(R.id.event_details_toolbar) Toolbar eventToolbar;
    @BindView(R.id.event_details_attendees_recycler_view) RecyclerView recyclerView;
    @BindView(R.id.event_details_static_map) ImageView staticMapImageView;

    @Override
    protected void initPresenter() {
        presenter = new EventDetailsPresenter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(eventToolbar);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
    }

    @Override
    public void enableHomeButton() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_event_details;
    }

    @Override
    public void initUserList() {
        List<UserProfile> attendees = getAttendees();
        adapter = new EventDetailsAttendeesAdapter(getBaseContext(), attendees);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
    
    public List<UserProfile> getAttendees() {
        // TODO: 05.06.2017
        List<UserProfile> attendees = new ArrayList<>();
        attendees.add(new UserProfile(null));
        attendees.add(new UserProfile(null));
        attendees.add(new UserProfile(null));
        attendees.add(new UserProfile(null));
        attendees.add(new UserProfile(null));
        attendees.add(new UserProfile(null));
        attendees.add(new UserProfile(null));
        attendees.add(new UserProfile(null));
        attendees.add(new UserProfile(null));
        attendees.add(new UserProfile(null));
        attendees.add(new UserProfile(null));
        attendees.add(new UserProfile(null));
        return attendees;
    }

    @Override
    public void loadStaticMap() {
        String latEiffelTower = "51.053945380663215";
        String lngEiffelTower = "17.060225307941437";
        String marker = "&markers=color:purple|" + latEiffelTower + "," + lngEiffelTower + "&";
        String url = "http://maps.google.com/maps/api/staticmap?center=" + latEiffelTower + "," + lngEiffelTower + "&zoom=15&size=800x400&sensor=false" + marker;
        Picasso.with(this).load(url).into(staticMapImageView);
    }
}
