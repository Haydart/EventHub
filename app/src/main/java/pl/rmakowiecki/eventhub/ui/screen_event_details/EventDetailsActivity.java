package pl.rmakowiecki.eventhub.ui.screen_event_details;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import butterknife.BindView;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import pl.rmakowiecki.eventhub.R;
import pl.rmakowiecki.eventhub.model.local.User;
import pl.rmakowiecki.eventhub.ui.BaseActivity;

public class EventDetailsActivity extends BaseActivity<EventDetailsPresenter> implements EventDetailsView {

    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @BindView(R.id.event_details_toolbar) Toolbar eventToolbar;
    @BindView(R.id.event_details_attendees_recycler_view) RecyclerView recyclerView;
    @BindView(R.id.event_details_static_map) ImageView staticMapImageView;
    @BindView(R.id.event_details_exp_text_view) ExpandableTextView expandableTextView;

    @Override
    protected void initPresenter() {
        presenter = new EventDetailsPresenter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(eventToolbar);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        initEventDescription();
    }

    private void initEventDescription() {
        expandableTextView.setText("Wacken Open Air (W:O:A) is a summer open-air heavy metal music festival. It takes place annually in the village of Wacken in Schleswig-Holstein, " +
                "northern Germany, 80 kilometres (50 mi) northwest of Hamburg.\n" +
                "\n" +
                "The festival was first held in 1990 as a small event for local German bands. W:O:A is usually held at the beginning of August and lasts four days. " +
                "It is currently considered the biggest heavy-metal festival in the world.[2] " +
                "In 2011, the festival attracted 80,000 festival visitors and 6,000 personnel for a total of roughly 86,000 attendees.[3]\n" +
                "\n" +
                "The festival traditionally ends on the first Sunday in August, and at midnight the following Monday tickets go on sale for the next year. " +
                "Remarkably, all 75,000 tickets were sold out within 43 hours for 2014, 12 hours for 2015, and 23 hours for 2016, despite the fact that the lineup " +
                "(with the exception of rumors or headliners) had not been announced. The non-optional basic ticket price for all four days, including camping, " +
                "was €180 in 2016. In 2015, 158 bands were playing on eight stages.[4] The international significance of the festival is shown by the attendees " +
                "in recent years being 30% foreigners, with up to 10% non-Europeans, from about 30-40 different countries all around the world. Many metal fans " +
                "travel from half a world away just to stand in cow meadows before stages set in the middle of nowhere.");
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
        List<User> attendees = getAttendees();
        adapter = new EventDetailsAttendeesAdapter(getBaseContext(), attendees);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    public List<User> getAttendees() {
        // TODO: 05.06.2017
        List<User> attendees = new ArrayList<>();
        attendees.add(new User("empty id", "empty name", new byte[] { 2, 1, 3, 7 }));
        attendees.add(new User("empty id", "empty name", new byte[] { 2, 1, 3, 7 }));
        attendees.add(new User("empty id", "empty name", new byte[] { 2, 1, 3, 7 }));
        attendees.add(new User("empty id", "empty name", new byte[] { 2, 1, 3, 7 }));
        attendees.add(new User("empty id", "empty name", new byte[] { 2, 1, 3, 7 }));
        attendees.add(new User("empty id", "empty name", new byte[] { 2, 1, 3, 7 }));
        attendees.add(new User("empty id", "empty name", new byte[] { 2, 1, 3, 7 }));
        attendees.add(new User("empty id", "empty name", new byte[] { 2, 1, 3, 7 }));
        attendees.add(new User("empty id", "empty name", new byte[] { 2, 1, 3, 7 }));
        attendees.add(new User("empty id", "empty name", new byte[] { 2, 1, 3, 7 }));
        return attendees;
    }

    @Override
    public void loadStaticMap() {
        String latitude = "51.053945380663215";
        String longitude = "17.060225307941437";
        String marker = "&markers=color:purple|" + latitude + "," + longitude + "&";
        String url = "http://maps.google.com/maps/api/staticmap?center=" + latitude + "," + longitude + "&zoom=15&size=800x400&sensor=false" + marker;
        Picasso.with(this).load(url).into(staticMapImageView);
    }
}
