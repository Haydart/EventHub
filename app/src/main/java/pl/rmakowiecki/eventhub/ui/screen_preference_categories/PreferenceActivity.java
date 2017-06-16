package pl.rmakowiecki.eventhub.ui.screen_preference_categories;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.OnClick;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import pl.rmakowiecki.eventhub.R;
import pl.rmakowiecki.eventhub.background.Constants;
import pl.rmakowiecki.eventhub.ui.BaseActivity;
import pl.rmakowiecki.eventhub.ui.custom_view.ActionButton;
import pl.rmakowiecki.eventhub.ui.screen_events_map.EventsMapActivity;
import pl.rmakowiecki.eventhub.ui.screen_preference_subcategories.PreferenceDetailsActivity;
import pl.rmakowiecki.eventhub.util.PreferencesManager;

import static pl.rmakowiecki.eventhub.util.FirebaseConstants.USER_DATA_REFERENCE;
import static pl.rmakowiecki.eventhub.util.FirebaseConstants.USER_PREFERENCES_REFERENCE;

public class PreferenceActivity extends BaseActivity<PreferencePresenter> implements PreferenceView {

    private static final int GRID_SPAN_COUNT = 2;
    private static final String SHARED_PREFERENCES_FIRST_LAUNCH_KEY = "is_first_launch";
    private static final String SHARED_PREFERENCES_KEY = "shared_preferences";

    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private PreferenceItemListener itemListener;
    private SharedPreferences sharedPreferences;
    private PreferencesManager preferencesManager;
    private List<PreferenceCategory> preferences;

    @BindView(R.id.preferences_recycler_view) RecyclerView recyclerView;
    @BindView(R.id.preferences_toolbar) Toolbar preferencesToolbar;
    @BindView(R.id.save_preferences_action_button) ActionButton savePreferencesButton;
    private ImageView sharedTransitionImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = new ArrayList<>();
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_KEY, MODE_PRIVATE);
        preferencesManager = new PreferencesManager(this);
        layoutManager = new GridLayoutManager(this, GRID_SPAN_COUNT);
        itemListener = (imageView, category) -> {
            sharedTransitionImage = imageView;
            presenter.onPreferenceImageClick(category);
        };
        setSupportActionBar(preferencesToolbar);
    }

    @Override
    protected void initPresenter() {
        presenter = new PreferencePresenter();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_preferences;
    }

    @Override
    public void initPreferences() {
        List<PreferenceCategory> preferenceCategories = getPreferenceCategories();
        adapter = new PreferenceAdapter(getBaseContext(), preferenceCategories, itemListener);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private List<PreferenceCategory> getPreferenceCategories() {
        preferences = preferencesManager.getPreferenceCategoryList();
        return preferences;
    }

    @Override
    public void launchPreferenceDetailsScreen(PreferenceCategory category) {
        Intent intent = new Intent(this, PreferenceDetailsActivity.class);
        intent.putExtra(Constants.PREFERENCE_CATEGORY_PARCEL_KEY, category);
        intent.putExtra(Constants.EXTRA_CATEGORY_IMAGE_TRANSITION_NAME, ViewCompat.getTransitionName(sharedTransitionImage));

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                sharedTransitionImage,
                ViewCompat.getTransitionName(sharedTransitionImage));
        startActivity(intent, options.toBundle());
    }

    @OnClick(R.id.save_preferences_action_button)
    protected void preferencesButtonClick() {
        presenter.onPreferenceSaveButtonClick(preferencesManager, preferences);
    }

    private Map<String, List<String>> getUserDataFromSharedPreferences() {
        Map<String, List<String>> categories = new HashMap<>();
        for (PreferenceCategory category : preferences) {
            Set<String> subCategories = preferencesManager.getInterests(category.getTitle());
            List<String> subCategoriesList = new ArrayList<>();
            subCategoriesList.addAll(subCategories);
            categories.put(category.getTitle(), subCategoriesList);
        }

        return categories;
    }

    @Override
    public void savePreferences() {
        savePreferencesButton.showProcessing();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            DatabaseReference userPreferencesRef = FirebaseDatabase
                    .getInstance()
                    .getReference(USER_DATA_REFERENCE)
                    .child(user.getUid())
                    .child(USER_PREFERENCES_REFERENCE);

            userPreferencesRef.setValue(getUserDataFromSharedPreferences());
        }

        presenter.onPreferenceSave();
    }

    @Override
    protected boolean shouldMoveToBack() {
        return true;
    }

    @Override
    public void enableHomeButton() {
        if (!isFirstLaunch()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    @Override
    public void showPreferencesSavingSuccess() {
        savePreferencesButton.showSuccess();
    }

    @Override
    public void launchMapAndFinish() {
        if (isFirstLaunch()) {
            unsetFirstLaunch();
            launchMapActivity();
        }
        finish();
    }

    @Override
    public void showNotEnoughPreferencesMessage() {
        Toast.makeText(this, getResources().getString(R.string.not_enough_preferences_msg), Toast.LENGTH_LONG).show();
    }

    private boolean isFirstLaunch() {
        return sharedPreferences.getBoolean(SHARED_PREFERENCES_FIRST_LAUNCH_KEY, true);
    }

    @SuppressLint("ApplySharedPref")
    private void unsetFirstLaunch() {
        sharedPreferences
                .edit()
                .putBoolean(SHARED_PREFERENCES_FIRST_LAUNCH_KEY, false)
                .commit();
    }

    private void launchMapActivity() {
        Intent intent = new Intent(this, EventsMapActivity.class);
        startActivity(intent);
    }
}
