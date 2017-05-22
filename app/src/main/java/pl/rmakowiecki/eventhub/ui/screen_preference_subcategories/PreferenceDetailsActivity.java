package pl.rmakowiecki.eventhub.ui.screen_preference_subcategories;

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
import pl.rmakowiecki.eventhub.ui.screen_preference_categories.PreferenceCategory;
import pl.rmakowiecki.eventhub.util.LocaleUtils;
import pl.rmakowiecki.eventhub.util.PreferencesManager;
import pl.rmakowiecki.eventhub.util.TransitionListenerAdapter;

public class PreferenceDetailsActivity extends BaseActivity<PreferenceDetailsPresenter> implements PreferenceDetailsView {

    @BindView(R.id.interests_recycler_view) RecyclerView recyclerView;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_layout) CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.toolbar_image) ImageView toolbarImage;
    @BindView(R.id.header_gradient_view) View headerImageGradientView;

    private PreferenceCategory category;
    private PreferenceInterestAdapter adapter;
    private PreferencesManager preferencesManager;
    private LocaleUtils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferencesManager = new PreferencesManager(this);
        utils = new LocaleUtils();
        supportPostponeEnterTransition();
        getPreferenceCategoryFromParcel();
        getSharedElementTransitionExtras();
        setSupportActionBar(toolbar);
        changeToolbarTitles();
        enableHomeButton();
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
                showAnimateHeaderGradient();
            }
        });
    }

    private void showAnimateHeaderGradient() {
        Animation slideUpAnimation = AnimationUtils.loadAnimation(PreferenceDetailsActivity.this, R.anim.slide_from_down_with_alpha);
        headerImageGradientView.startAnimation(slideUpAnimation);
        headerImageGradientView.setVisibility(View.VISIBLE);
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
        String categoryName = category.getTitle();
        if (utils.hasLocale())
            categoryName = preferencesManager.getNameOrLocaleName(utils.getLocaleString(), categoryName, categoryName);
        collapsingToolbarLayout.setTitle(categoryName);
    }

    @Override
    public void loadAdapter() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        adapter = new PreferenceInterestAdapter(this, category.getChildList(), preferencesManager.getInterests(category.getTitle()), preferencesManager,
                utils.getLocaleString(), category.getTitle());
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
        preferencesManager.saveSubCategories(category.getTitle(), subCategories);
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
