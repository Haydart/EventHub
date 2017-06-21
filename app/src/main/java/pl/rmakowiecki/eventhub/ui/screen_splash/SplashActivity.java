package pl.rmakowiecki.eventhub.ui.screen_splash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import butterknife.BindView;
import com.wang.avi.AVLoadingIndicatorView;
import java.util.List;
import pl.rmakowiecki.eventhub.R;
import pl.rmakowiecki.eventhub.model.local.Interest;
import pl.rmakowiecki.eventhub.model.local.PreferenceLocale;
import pl.rmakowiecki.eventhub.model.local.User;
import pl.rmakowiecki.eventhub.ui.BaseActivity;
import pl.rmakowiecki.eventhub.ui.screen_events_map.EventsMapActivity;
import pl.rmakowiecki.eventhub.ui.screen_preference_categories.PreferenceActivity;
import pl.rmakowiecki.eventhub.ui.screen_preference_categories.PreferenceCategory;
import pl.rmakowiecki.eventhub.util.PreferencesManager;
import pl.rmakowiecki.eventhub.util.ViewAnimationListenerAdapter;

import static pl.rmakowiecki.eventhub.background.Constants.SHARED_PREFERENCES_KEY;

public class SplashActivity extends BaseActivity<SplashPresenter> implements SplashView {

    private final String SHARED_PREFERENCES_FIRST_LAUNCH_KEY = "is_first_launch";

    @BindView(R.id.splash_image) ImageView splashImageView;
    @BindView(R.id.loading_view) AVLoadingIndicatorView loadingIndicatorView;

    private SharedPreferences sharedPreferences;
    private PreferencesManager preferencesManager;
    private boolean isFirstLaunch = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferencesManager = new PreferencesManager(this);
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_KEY, MODE_PRIVATE);
    }

    @Override
    public void showSplashAnimation() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.splash_anim);
        animation.setAnimationListener(new ViewAnimationListenerAdapter() {
            @Override
            public void onAnimationEnd(Animation animation) {
                super.onAnimationEnd(animation);
                loadingIndicatorView.setVisibility(View.VISIBLE);
                loadingIndicatorView.startAnimation(AnimationUtils.loadAnimation(SplashActivity.this, R.anim.fade_in));
            }
        });
        splashImageView.startAnimation(animation);
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
        startActivity(intent);
        finish();
    }

    @Override
    public void savePreferences(List<PreferenceCategory> preferenceList) {
        preferencesManager.saveCategories(preferenceList);
        presenter.onComponentLoaded();
    }

    @Override
    public void saveInterests(List<Interest> interests) {
        preferencesManager.saveInterests(interests);
    }

    @Override
    public void saveLocales(List<PreferenceLocale> localesList) {
        preferencesManager.saveLocales(localesList);
    }

    @Override
    public void saveUserData(User user) {
        preferencesManager.saveUserDataLocally(user);
    }

    @Override
    public void checkIfFirstLaunch() {
        if (sharedPreferences.getBoolean(SHARED_PREFERENCES_FIRST_LAUNCH_KEY, true)) {
            isFirstLaunch = true;
        }
    }
}
