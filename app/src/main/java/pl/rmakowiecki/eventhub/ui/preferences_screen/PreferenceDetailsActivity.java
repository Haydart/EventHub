package pl.rmakowiecki.eventhub.ui.preferences_screen;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.OnClick;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import pl.rmakowiecki.eventhub.R;
import pl.rmakowiecki.eventhub.background.Constants;
import pl.rmakowiecki.eventhub.ui.BaseActivity;

import static pl.rmakowiecki.eventhub.util.FirebaseConstants.USER_DATA_REFERENCE;
import static pl.rmakowiecki.eventhub.util.FirebaseConstants.USER_PREFERENCES_REFERENCE;

public class PreferenceDetailsActivity extends BaseActivity implements PreferenceDetailsView {

    @BindView(R.id.interests_recycler_view) RecyclerView recyclerView;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_layout) CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.toolbar_image) ImageView toolbarImage;

    private PreferenceCategory category;
    private PreferenceInterestAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            String imageTransitionName = extras.getString(Constants.EXTRA_CATEGORY_IMAGE_TRANSITION_NAME);
            toolbarImage.setTransitionName(imageTransitionName);
        }
    }

    @Override
    public void getPreferenceCategoryFromParcel() {
        category = getIntent().getParcelableExtra(Constants.PREFERENCE_CATEGORY_PARCEL_KEY);
    }

    @Override
    public void displayToolbarImage() {
        int resourceID = getResources()
                .getIdentifier(category.getImageResourceName(), "drawable", getPackageName());

        Picasso.with(this)
                .load(resourceID != 0 ? resourceID : R.drawable.ic_image_placeholder)
                .noFade()
                .placeholder(R.drawable.ic_image_placeholder)
                .into(toolbarImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        supportStartPostponedEnterTransition();
                    }

                    @Override
                    public void onError() {
                        supportStartPostponedEnterTransition();
                    }
                });
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
        adapter = new PreferenceInterestAdapter(this, category.getChildList());
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
        List<String> subCategories = adapter.getCheckedSubCategories();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            DatabaseReference userPreferencesRef = FirebaseDatabase
                            .getInstance()
                            .getReference(USER_DATA_REFERENCE)
                            .child(user.getUid())
                            .child(USER_PREFERENCES_REFERENCE);

            Map<String, List<String>> categories = new HashMap<>();
            categories.put(category.getTitle(), subCategories);
            userPreferencesRef.setValue(categories);
        } else {
            // TODO: 11.04.2017  Handle case when user is not logged in (save preferences temporarily)
        }

        finish();
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
