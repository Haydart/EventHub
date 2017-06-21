package pl.rmakowiecki.eventhub.ui.screen_event_calendar;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by m1per on 09.04.2017.
 */
public class EventsFragmentAdapter extends FragmentPagerAdapter {

    private final int PAGE_COUNT = 3;

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
        switch (position) {
            case 0:
                return PersonalizedEventsFragment.newInstance(position);
            case 1:
                return MyEventsFragment.newInstance(position);
            case 2:
                return EventsFragment.newInstance(position);
            default:
                return EventsFragment.newInstance(position);
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
