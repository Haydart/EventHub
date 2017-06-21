package pl.rmakowiecki.eventhub.ui.screen_event_calendar;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import net.danlew.android.joda.JodaTimeAndroid;
import pl.rmakowiecki.eventhub.R;
import pl.rmakowiecki.eventhub.model.local.Event;
import pl.rmakowiecki.eventhub.ui.BaseActivity;
import pl.rmakowiecki.eventhub.util.SortTypes;

public class CalendarActivity extends BaseActivity<CalendarPresenter>
        implements CalendarView, EventsFragment.OnListFragmentInteractionListener,
        MyEventsFragment.OnListFragmentInteractionListener,
        PersonalizedEventsFragment.OnListFragmentInteractionListener {

    @BindView(R.id.toolbar) Toolbar calendarToolbar;
    @BindView(R.id.viewpager) ViewPager viewPager;
    @BindView(R.id.tab_layout) TabLayout tabLayout;
    @BindString(R.string.title_events) String calendarScreenTitle;
    @BindString(R.string.tab_title_all_events) String allEventsTabTitle;
    @BindString(R.string.tab_title_my_events) String myEventsTabTitle;
    @BindString(R.string.tab_title_personalized_events) String personalizedEventsTabTitle;
    private String tabTitles[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        tabTitles = new String[] { personalizedEventsTabTitle, myEventsTabTitle, allEventsTabTitle };
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sorting_menu_item_sooner:
                presenter.onSortOptionSelected(SortTypes.DATE_SORT);
                return true;
            case R.id.sorting_menu_item_closer:
                presenter.onSortOptionSelected(SortTypes.DISTANCE_SORT);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void initPresenter() {
        presenter = new CalendarPresenter();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_calendar;
    }

    @Override
    public void onListFragmentInteraction(Event event) {
        //no-op
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.calendar_menu, menu);
        return true;
    }

    @Override
    public void sortEvents(SortTypes sortType) {
        PersonalizedEventsFragment personalizedEventsFragment =
                (PersonalizedEventsFragment) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":0");
        MyEventsFragment filteredEventsFragment = (MyEventsFragment) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":1");
        EventsFragment allEventsFragment = (EventsFragment) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":2");

        personalizedEventsFragment.sortEvents(sortType);
        filteredEventsFragment.sortEvents(sortType);
        allEventsFragment.sortEvents(sortType);
    }
}
