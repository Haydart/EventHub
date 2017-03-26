package pl.rmakowiecki.eventhub.ui.preferences_screen;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import pl.rmakowiecki.eventhub.R;
import pl.rmakowiecki.eventhub.model.local.Preference;
import pl.rmakowiecki.eventhub.ui.BaseActivity;

public class PreferenceActivity extends BaseActivity<PreferencePresenter> implements PreferenceView {

    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @BindView(R.id.preferencesRecyclerView)RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layoutManager = new LinearLayoutManager(this);
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
        for (Preference preference : preferences)
        {
            PreferenceCategory category = new PreferenceCategory(preference.getName(), preference.getImageURL());
            for (String interestName : preference.getSubCategories())
                category.addChildObject(new PreferenceInterest(interestName, false));
            categories.add(category);
        }
        initPreferences(categories);
    }

    @Override
    public void initPreferences(final List<PreferenceCategory> categories) {
        adapter = new PreferenceAdapter(getBaseContext(), categories);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}
