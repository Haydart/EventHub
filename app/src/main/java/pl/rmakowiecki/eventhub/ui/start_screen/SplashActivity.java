package pl.rmakowiecki.eventhub.ui.start_screen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Bundle;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import pl.rmakowiecki.eventhub.R;
import pl.rmakowiecki.eventhub.model.local.Preference;
import pl.rmakowiecki.eventhub.ui.BaseActivity;
import pl.rmakowiecki.eventhub.ui.events_map_screen.EventsMapActivity;
import pl.rmakowiecki.eventhub.ui.preferences_screen.PreferenceActivity;
import pl.rmakowiecki.eventhub.ui.preferences_screen.PreferenceCategory;
import pl.rmakowiecki.eventhub.ui.preferences_screen.PreferenceModelMapper;

public class SplashActivity extends BaseActivity<SplashPresenter> implements SplashView {

    @BindString(R.string.preference_category) String preferenceCategoryString;

    private final int LOADING_TIME = 5000; // TODO: 14.04.2017 Remove hardcoded value
    private final String SHARED_PREFERENCES_FIRST_LAUNCH_KEY = "is_first_launch"; // TODO: 15.04.2017 Figure out a better key
    private final String SHARED_PREFERENCES_KEY = "shared_preferences"; // TODO: 15.04.2017 Figure out a better key

    private boolean firstLaunch = false;
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
    public void launchAppDelayed() {
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, firstLaunch ? PreferenceActivity.class : EventsMapActivity.class);
            intent.putParcelableArrayListExtra(preferenceCategoryString, (ArrayList<? extends Parcelable>) preferences);
            startActivity(intent);
            finish();
        }, LOADING_TIME);
    }

    @Override
    public void savePreferences(List<Preference> preferenceList) {
        List<PreferenceCategory> categories = new ArrayList<>();
        PreferenceModelMapper mapper = new PreferenceModelMapper();
        for (Preference preference : preferenceList) {
            categories.add(mapper.map(preference));
        }

        preferences = categories;
    }

    @Override
    public void checkIfFirstLaunch() {
        SharedPreferences preferences = getSharedPreferences(SHARED_PREFERENCES_KEY, MODE_PRIVATE);
        if (preferences.getBoolean(SHARED_PREFERENCES_FIRST_LAUNCH_KEY, true)) {
            firstLaunch = true;
            preferences.edit().putBoolean(SHARED_PREFERENCES_FIRST_LAUNCH_KEY, false).commit();
        }
    }
}
