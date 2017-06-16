package pl.rmakowiecki.eventhub.ui.screen_event_details;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;
import pl.rmakowiecki.eventhub.R;
import pl.rmakowiecki.eventhub.model.local.Event;
import pl.rmakowiecki.eventhub.model.local.EventAttendee;
import pl.rmakowiecki.eventhub.ui.BaseActivity;
import pl.rmakowiecki.eventhub.util.DateUtils;
import pl.rmakowiecki.eventhub.util.UserAuthManager;

import static pl.rmakowiecki.eventhub.background.Constants.EVENT_DETAILS_MORE_USERS;
import static pl.rmakowiecki.eventhub.background.Constants.EVENT_DETAILS_PARCEL_KEY;

public class EventDetailsActivity extends BaseActivity<EventDetailsPresenter> implements EventDetailsView {

    private final int MAX_DISPLAYED_USERS = 20;
    @BindView(R.id.event_details_toolbar) Toolbar eventToolbar;
    @BindView(R.id.event_details_attendees_recycler_view) RecyclerView recyclerView;
    @BindView(R.id.event_details_static_map) ImageView staticMapImageView;
    @BindView(R.id.event_details_exp_text_view) ExpandableTextView expandableTextView;
    @BindView(R.id.event_details_date) TextView dateTextView;
    @BindView(R.id.event_details_place) TextView placeTextView;
    @BindView(R.id.event_details_time) TextView timeTextView;
    @BindView(R.id.event_details_organiser) TextView organiserTextView;
    @BindView(R.id.event_details_name) TextView nameTextView;
    @BindView(R.id.event_details_attendees_text_view) TextView attendeesTextView;
    @BindView(R.id.event_details_no_attendees_text_view) TextView noAttendeesTextView;
    @BindView(R.id.event_details_attendees_layout) LinearLayout attendeesLinearLayout;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private Event event;

    @Override
    protected void initPresenter() {
        presenter = new EventDetailsPresenter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        readEventFromBundle();
        setupToolbar();
    }

    @Override
    public void initEventDetails() {
        initEventInfo();
        initEventDescription();
        initStaticMap();
        initUserList();
    }

    private void readEventFromBundle() {
        Bundle extras = getIntent().getExtras();
        event = extras.getParcelable(EVENT_DETAILS_PARCEL_KEY);
    }

    private void setupToolbar() {
        eventToolbar.setTitle(R.string.event_details_title);
        setSupportActionBar(eventToolbar);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
    }

    private void initEventInfo() {
        DateTime dateOfEvent = new DateTime(event.getTimestamp());
        dateTextView.setText(getBaseContext().getString(R.string.event_details_date) + ":   " + DateUtils.getFormattedDate(dateOfEvent, "dd/MM/yyyy"));
        timeTextView.setText(getBaseContext().getString(R.string.event_details_time) + ":   " + DateUtils.getFormattedDate(dateOfEvent, "HH:mm"));
        placeTextView.setText(getBaseContext().getString(R.string.event_details_address) + ":   " + event.getAddress());
        organiserTextView.setText(getBaseContext().getString(R.string.event_details_organiser) + ":   " + event.getOrganizer());
        nameTextView.setText(getBaseContext().getString(R.string.event_details_name) + ":   " + event.getName());
    }

    private void initEventDescription() {
        expandableTextView.setText(event.getDescription());
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

    private void initUserList() {
        List<EventAttendee> attendees = getAttendees();

        if (!attendees.isEmpty()) {
            adapter = new EventDetailsAttendeesAdapter(getBaseContext(), attendees);
            setupRecyclerView();
            attendeesTextView.setText(getBaseContext().getString(R.string.event_details_attendees_count) + ": " + attendees.size());
            changeAttendeesVisibility(true);
        } else {
            changeAttendeesVisibility(false);
        }
    }

    private void changeAttendeesVisibility(boolean showList) {
        noAttendeesTextView.setVisibility(showList ? View.INVISIBLE : View.VISIBLE);
        attendeesLinearLayout.setVisibility(showList ? View.VISIBLE : View.INVISIBLE);
    }

    private void setupRecyclerView() {
        recyclerView.getRecycledViewPool().setMaxRecycledViews(0, 0);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private List<EventAttendee> getAttendees() {
        List<EventAttendee> attendees = new ArrayList<>();
        UserAuthManager userAuthManager = new UserAuthManager();
        String userId = userAuthManager.getCurrentUserId();

        for (EventAttendee attendee : event.getAttendees()) {
            if (!attendee.getId().equals(userId)) {
                if (attendees.size() >= MAX_DISPLAYED_USERS) {
                    attendees.add(new EventAttendee("FULL LIST", EVENT_DETAILS_MORE_USERS));
                    break;
                }
                attendees.add(attendee);
                break;
            }
        }

        return attendees;
    }

    private void initStaticMap() {
        String location = event.getLocationCoordinates().replaceAll("\\s+", "");
        String marker = "&markers=color:purple|" + location + "&";
        String url = "http://maps.google.com/maps/api/staticmap?center=" + location + "&zoom=15&size=800x400&sensor=false" + marker;
        Picasso
                .with(this)
                .load(url)
                .into(staticMapImageView);
    }
}
