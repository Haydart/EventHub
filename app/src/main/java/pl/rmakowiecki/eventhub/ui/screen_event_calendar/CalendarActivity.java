package pl.rmakowiecki.eventhub.ui.screen_event_calendar;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import net.danlew.android.joda.JodaTimeAndroid;
import pl.rmakowiecki.eventhub.R;
import pl.rmakowiecki.eventhub.model.local.Event;
import pl.rmakowiecki.eventhub.ui.BaseActivity;

public class CalendarActivity extends BaseActivity<CalendarPresenter>
        implements CalendarView, EventsFragment.OnListFragmentInteractionListener, MyEventsFragment.OnListFragmentInteractionListener {

    @BindView(R.id.toolbar) Toolbar calendarToolbar;
    @BindView(R.id.viewpager) ViewPager viewPager;
    @BindView(R.id.tab_layout) TabLayout tabLayout;
    @BindString(R.string.title_events) String calendarScreenTitle;
    @BindString(R.string.tab_title_all_events) String allEventsTabTitle;
    @BindString(R.string.tab_title_my_events) String myEventsTabTitle;
    private String tabTitles[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        tabTitles = new String[] { allEventsTabTitle, myEventsTabTitle };
        viewPager.setAdapter(new EventsFragmentAdapter(getSupportFragmentManager(),
                CalendarActivity.this, tabTitles));

        tabLayout.setupWithViewPager(viewPager);

        calendarToolbar.setTitle(calendarScreenTitle);
        setSupportActionBar(calendarToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        JodaTimeAndroid.init(this);
    }

    @Override
    protected void initPresenter() {
        //no-op
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_calendar;
    }

    @Override
    public void onListFragmentInteraction(Event event) {
        //TODO: will be used and named properly during list actions development
    }
}
