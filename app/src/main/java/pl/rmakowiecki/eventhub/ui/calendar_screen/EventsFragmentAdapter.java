package pl.rmakowiecki.eventhub.ui.calendar_screen;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import butterknife.BindString;
import pl.rmakowiecki.eventhub.R;

/**
 * Created by m1per on 09.04.2017.
 */
public class EventsFragmentAdapter extends FragmentPagerAdapter {


    private final int PAGE_COUNT = 2;
    @BindString(R.string.tab_title_all_events) String allEventsTabTitle;
    @BindString(R.string.tab_title_my_events) String myEventsTabTitle;

    private String tabTitles[] = new String[] { allEventsTabTitle, myEventsTabTitle};

    public EventsFragmentAdapter(FragmentManager fm, Context context) {
        super(fm);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        return EventsFragment.newInstance(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
