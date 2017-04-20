package pl.rmakowiecki.eventhub.ui.calendar_screen;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import butterknife.BindView;
import pl.rmakowiecki.eventhub.R;
import pl.rmakowiecki.eventhub.ui.BaseActivity;

public class CalendarActivity extends BaseActivity<CalendarPresenter> implements CalendarView, EventsFragment.OnListFragmentInteractionListener {

    @BindView(R.id.calendar_toolbar) Toolbar calendarToolbar;
    @BindView(R.id.viewpager) ViewPager vPager;
    @BindView(R.id.sliding_tabs) TabLayout slidingTabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewPager viewPager = vPager;
        viewPager.setAdapter(new EventsFragmentAdapter(getSupportFragmentManager(),
                CalendarActivity.this));

        TabLayout tabLayout = slidingTabs;
        tabLayout.setupWithViewPager(viewPager);
        calendarToolbar.setTitle("Wydarzenia"); //TODO: Extract to resource string
        setSupportActionBar(calendarToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    protected void initPresenter() {
    }

    @Override
    protected int getLayoutResId() { return R.layout.activity_calendar; }

    @Override
    public void onListFragmentInteraction(Event event) {
    }
}
