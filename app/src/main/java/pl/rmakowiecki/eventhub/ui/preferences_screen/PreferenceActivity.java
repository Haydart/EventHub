package pl.rmakowiecki.eventhub.ui.preferences_screen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import butterknife.BindView;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;
import pl.rmakowiecki.eventhub.R;
import pl.rmakowiecki.eventhub.ui.BaseActivity;
import pl.rmakowiecki.eventhub.ui.events_map_screen.EventsMapActivity;

public class PreferenceActivity extends BaseActivity<PreferencePresenter> implements PreferenceView {

    private static final int GRID_SPAN_COUNT = 2;
    private static final String PREFERENCE_CATEGORY_PARCEL_KEY = "preference_category";
    private static final String SHARED_PREFERENCES_FIRST_LAUNCH_KEY = "is_first_launch";
    private static final String SHARED_PREFERENCES_KEY = "shared_preferences";

    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private PreferenceItemListener itemListener;
    private List<PreferenceCategory> preferences;
    private SharedPreferences sharedPreferences;

    @BindView(R.id.preferences_recycler_view) RecyclerView recyclerView;
    @BindView(R.id.preferences_toolbar) Toolbar preferencesToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_KEY, MODE_PRIVATE);
        layoutManager = new GridLayoutManager(this, GRID_SPAN_COUNT);
        itemListener = category -> presenter.onPreferenceImageClick(category);
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
    public void saveParcelData() {
        preferences = getIntent().getParcelableArrayListExtra(PREFERENCE_CATEGORY_PARCEL_KEY);
        initPreferences(preferences);
    }

    @Override
    public void initPreferences(final List<PreferenceCategory> categories) {
        adapter = new PreferenceAdapter(getBaseContext(), categories, itemListener);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void launchPreferenceDetailsScreen(PreferenceCategory category) {
        Intent intent = new Intent(getBaseContext(), PreferenceDetailsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(PREFERENCE_CATEGORY_PARCEL_KEY, category);
        startActivity(intent);
    }

    @OnClick (R.id.save_preferences_action_button)
    protected void preferencesButtonClick() {
        presenter.onPreferenceSaveButtonClick();
    }

    @Override
    public void savePreferences() {
        if (isFirstLaunch()) {
            unsetFirstLaunch();
            launchMapActivity();
        }
        finish();
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

    private boolean isFirstLaunch() {
        return sharedPreferences.getBoolean(SHARED_PREFERENCES_FIRST_LAUNCH_KEY, true);
    }

    private void unsetFirstLaunch() {
        sharedPreferences
                .edit()
                .putBoolean(SHARED_PREFERENCES_FIRST_LAUNCH_KEY, false)
                .commit();
    }

    private void launchMapActivity() {
        Intent intent = new Intent(this, EventsMapActivity.class);
        intent.putParcelableArrayListExtra(PREFERENCE_CATEGORY_PARCEL_KEY, (ArrayList<? extends Parcelable>) preferences);
        startActivity(intent);
    }
}
