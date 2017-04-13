package pl.rmakowiecki.eventhub.ui.preferences_screen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import butterknife.BindString;
import butterknife.BindView;
import java.util.ArrayList;
import java.util.List;
import pl.rmakowiecki.eventhub.R;
import pl.rmakowiecki.eventhub.model.local.Preference;
import pl.rmakowiecki.eventhub.ui.BaseActivity;

public class PreferenceActivity extends BaseActivity<PreferencePresenter> implements PreferenceView {

    private static final int GRID_SPAN_COUNT = 2;

    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private PreferenceItemListener itemListener;

    @BindView(R.id.preferences_recycler_view) RecyclerView recyclerView;
    @BindView(R.id.preferences_toolbar) Toolbar preferencesToolbar;
    @BindString(R.string.preference_category) String preferenceCategoryString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
    public void showPreferences(List<Preference> preferences) {
        List<PreferenceCategory> categories = new ArrayList<>();
        PreferenceModelMapper mapper = new PreferenceModelMapper();
        for (Preference preference : preferences) {
            categories.add(mapper.map(preference));
        }

        initPreferences(categories);
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
        intent.putExtra(preferenceCategoryString, category);
        startActivity(intent);
    }
}
