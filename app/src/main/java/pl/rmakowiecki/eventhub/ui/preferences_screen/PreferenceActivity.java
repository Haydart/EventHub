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

        // Recycler View
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PreferenceAdapter(getBaseContext(), generateCategories());
        recyclerView.setAdapter(adapter);
    }

    private List<PreferenceCategory> generateCategories() {

        // TODO: 2017-03-16 Load categories from server
        // TODO: 2017-03-16 Remove debug categories
        PreferenceCategory sport = new PreferenceCategory("SPORT");
        sport.addChildObject(new PreferenceInterest("Piłka nożna", false));
        sport.addChildObject(new PreferenceInterest("Koszykówka", false));

        PreferenceCategory muzyka = new PreferenceCategory("MUZYKA");
        muzyka.addChildObject(new PreferenceInterest("Klasyczna", false));
        muzyka.addChildObject(new PreferenceInterest("Metal", false));

        List<PreferenceCategory> categoriesList = new ArrayList<>();
        categoriesList.add(sport);
        categoriesList.add(muzyka);
        return categoriesList;
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
    public void showPreferences(List<Preference> preferenceList) {
        // TODO: 2017-03-16
    }
}
