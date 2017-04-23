package pl.rmakowiecki.eventhub.ui.preferences_screen;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.OnClick;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import java.util.HashSet;
import java.util.Set;
import pl.rmakowiecki.eventhub.R;
import pl.rmakowiecki.eventhub.background.Constants;
import pl.rmakowiecki.eventhub.ui.BaseActivity;
import pl.rmakowiecki.eventhub.util.TransitionListenerAdapter;

public class PreferenceDetailsActivity extends BaseActivity implements PreferenceDetailsView {

    @BindView(R.id.interests_recycler_view) RecyclerView recyclerView;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_layout) CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.toolbar_image) ImageView toolbarImage;
    @BindView(R.id.header_gradient_view) View headerImageGradientView;

    private PreferenceCategory category;
    private PreferenceInterestAdapter adapter;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES_KEY, MODE_PRIVATE);
        getSharedElementTransitionExtras();
        animateHeaderGradient();
    }

    private void getSharedElementTransitionExtras() {
        Bundle extras = getIntent().getExtras();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            String imageTransitionName = extras.getString(Constants.EXTRA_CATEGORY_IMAGE_TRANSITION_NAME);
            toolbarImage.setTransitionName(imageTransitionName);
        }
    }

    private void animateHeaderGradient() {
        Transition sharedElementEnterTransition = getWindow().getSharedElementEnterTransition();
        sharedElementEnterTransition.addListener(new TransitionListenerAdapter() {
            @Override
            public void onTransitionEnd(@NonNull Transition transition) {
                super.onTransitionEnd(transition);
                Animation slideUpAnimation = AnimationUtils.loadAnimation(PreferenceDetailsActivity.this, R.anim.slide_from_down);
                headerImageGradientView.startAnimation(slideUpAnimation);
                headerImageGradientView.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void getPreferenceCategoryFromParcel() {
        category = getIntent().getParcelableExtra(Constants.PREFERENCE_CATEGORY_PARCEL_KEY);
    }

    @Override
    public void displayToolbarImage() {
        int resourceID = getResources()
                .getIdentifier(category.getImageResourceName(), Constants.DRAWABLE_REFERENCE, getPackageName());

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
        adapter = new PreferenceInterestAdapter(this, category.getChildList(), sharedPreferences.getStringSet(category.getTitle(), new HashSet<>()));
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
        Set<String> subCategories = new HashSet<>();
        subCategories.addAll(adapter.getCheckedSubCategories());
        sharedPreferences.edit().remove(category.getTitle());
        sharedPreferences.edit().putStringSet(category.getTitle(), subCategories).commit();

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
