package pl.rmakowiecki.eventhub.ui.screen_splash;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import pl.rmakowiecki.eventhub.R;
import pl.rmakowiecki.eventhub.background.Constants;
import pl.rmakowiecki.eventhub.model.local.Interest;
import pl.rmakowiecki.eventhub.model.local.Preference;
import pl.rmakowiecki.eventhub.ui.BaseActivity;
import pl.rmakowiecki.eventhub.ui.screen_events_map.EventsMapActivity;
import pl.rmakowiecki.eventhub.ui.screen_preference_categories.PreferenceActivity;
import pl.rmakowiecki.eventhub.ui.screen_preference_categories.PreferenceCategory;
import pl.rmakowiecki.eventhub.ui.screen_preference_categories.PreferenceModelMapper;

public class SplashActivity extends BaseActivity<SplashPresenter> implements SplashView {

    private final String SHARED_PREFERENCES_FIRST_LAUNCH_KEY = "is_first_launch";
    private final String PREFERENCE_CATEGORY_PARCEL_KEY = "preference_category";

    private boolean isFirstLaunch = false;
    private boolean hasLoadedPreferences = false;
    private boolean hasLoadedInterests = false;
    private List<PreferenceCategory> preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initPresenter() {
        presenter = new SplashPresenter();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_splash;
    }

    @Override
    public void launchApplication() {
        Intent intent = new Intent(this, isFirstLaunch ? PreferenceActivity.class : EventsMapActivity.class);
        intent.putParcelableArrayListExtra(PREFERENCE_CATEGORY_PARCEL_KEY, (ArrayList<? extends Parcelable>) preferences);
        startActivity(intent);
        finish();
    }

    @Override
    public void savePreferences(List<Preference> preferenceList) {
        List<PreferenceCategory> categories = new ArrayList<>();
        PreferenceModelMapper mapper = new PreferenceModelMapper();
        for (Preference preference : preferenceList) {
            categories.add(mapper.map(preference));
        }

        preferences = categories;
        hasLoadedPreferences = true;
        launchApplication();
    }

    @SuppressLint("ApplySharedPref")
    @Override
    public void saveInterests(List<Interest> interests) {
        hasLoadedInterests = true;

        if (!interests.isEmpty()) {
            SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES_KEY, MODE_PRIVATE);
            for (Interest interest : interests) {
                Set<String> subCategories = new HashSet<>();
                subCategories.addAll(interest.getSubCategories());
                sharedPreferences.
                        edit()
                        .putStringSet(interest.getName(), subCategories)
                        .commit();
            }
        }
        launchApplication();
    }

    @Override
    public void checkIfFirstLaunch() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES_KEY, MODE_PRIVATE);
        if (sharedPreferences.getBoolean(SHARED_PREFERENCES_FIRST_LAUNCH_KEY, true)) {
            isFirstLaunch = true;
        }
    }
}