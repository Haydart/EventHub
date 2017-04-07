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

public class PreferenceDetails extends BaseActivity implements PreferenceDetailsView {

    @BindView(R.id.interests_recycler_view) RecyclerView recyclerView;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_layout) CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.toolbar_image) ImageView toolbarImage;
    @BindString(R.string.preference_category) String parcelCategoryString;

    private PreferenceCategory category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void getPreferenceCategoryFromParcel() {
        category = getIntent().getParcelableExtra(parcelCategoryString);
    }

    @Override
    public void displayToolbarImage() {
        Picasso
                .with(getBaseContext())
                .load(category.getImageUrl())
                .placeholder(R.drawable.ic_image_placeholder)
                .into(toolbarImage);
    }

    @Override
    public void changeToolbarTitles() {
        toolbar.setTitle("");
        collapsingToolbarLayout.setTitle(category.getTitle());
        setSupportActionBar(toolbar);
    }

    @Override
    public void loadAdapter() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        RecyclerView.Adapter adapter = new PreferenceInterestAdapter(getBaseContext(), category.getChildList());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void enableHomeButton() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @OnClick(R.id.preference_details_fab)
    public void onFloatingActionButtonClick(View v) {
        // TODO: 05.04.2017
    }

    @Override
    protected void initPresenter() {
        presenter = new PreferenceDetailsPresenter();
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
