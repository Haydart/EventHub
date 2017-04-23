package pl.rmakowiecki.eventhub.ui.calendar_screen;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by m1per on 09.04.2017.
 */
public class EventsFragmentAdapter extends FragmentPagerAdapter {

    private final int PAGE_COUNT = 2;

    private String tabTitles[];

    public EventsFragmentAdapter(FragmentManager fm, Activity activity, String[] titles) {
        super(fm);
        tabTitles = titles;
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
