package pl.rmakowiecki.eventhub.ui.preferences_screen;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import pl.rmakowiecki.eventhub.R;
import pl.rmakowiecki.eventhub.ui.BaseActivity;

public class PreferenceDetails extends BaseActivity {

    @BindView(R.id.interestsRecyclerView) RecyclerView recyclerView;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_layout) CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.toolbarImage) ImageView toolbarImage;
    @BindString(R.string.preference_category) String parcelCategoryString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PreferenceCategory category = getIntent().getParcelableExtra(parcelCategoryString);
        displayToolbarImage(category);
        changeToolbarTitles(category);
        loadAdapter(category);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void displayToolbarImage(PreferenceCategory category) {
        Picasso
                .with(getBaseContext())
                .load(category.getImageUrl())
                .placeholder(R.drawable.ic_image_placeholder)
                .into(toolbarImage);
    }

    private void changeToolbarTitles(PreferenceCategory category) {
        toolbar.setTitle("");
        collapsingToolbarLayout.setTitle(category.getTitle());
        setSupportActionBar(toolbar);
    }

    private void loadAdapter(PreferenceCategory category) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        RecyclerView.Adapter adapter = new PreferenceInterestAdapter(getBaseContext(), category.getChildList());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    @OnClick(R.id.preference_details_fab)
    public void onFloatingActionButtonClick(View v) {
        // TODO: 05.04.2017
    }

    @Override
    protected void initPresenter() {
        // TODO: 05.04.2017 create view and presenter
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_preference_details;
    }

    @Override
    protected boolean shouldMoveToBack() {
        return true;
    }
}
