package pl.rmakowiecki.eventhub.ui.screen_app_features;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.github.florent37.arclayout.ArcLayout;
import com.google.firebase.auth.FirebaseAuth;
import pl.rmakowiecki.eventhub.R;
import pl.rmakowiecki.eventhub.ui.BaseActivity;
import pl.rmakowiecki.eventhub.ui.custom_view.ActionButton;
import pl.rmakowiecki.eventhub.ui.screen_auth.AuthActivity;
import pl.rmakowiecki.eventhub.util.ViewAnimationListenerAdapter;

public class AppFeaturesActivity extends BaseActivity<AppFeaturesPresenter> implements AppFeaturesView, ViewPagerEx.OnPageChangeListener {

    @BindView(R.id.features_image_slider) SliderLayout featuresSliderLayout;
    @BindView(R.id.feature_description_text_view) TextView featureDescriptionTextView;
    @BindView(R.id.custom_indicator) PagerIndicator pagerIndicator;
    @BindView(R.id.sign_up_action_button) ActionButton signUpActionButton;
    @BindView(R.id.content_layout) LinearLayout contentLayout;
    @BindView(R.id.arc_layout) ArcLayout arcLayout;

    private FirebaseAuth firebaseAuth;
    private int[] featuresRedIds = {
            R.drawable.dziad,
            R.drawable.mudzin,
            R.drawable.wielblad
    };
    // TODO: 01/05/2017 remove temporary imagess & descriptions
    private String[] featureDescriptionTexts = {
            "Nasza apka jest dobra dla starych ludzi.",
            "Nasza apka jest dobra dla murzynów.",
            "Nasza apka jest dobra dla wielbłądów."
    };
    ;

    @OnClick(R.id.sign_up_action_button)
    public void onSignUpButtonClicked() {
        presenter.onSignUpButtonClicked();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        initImageSlider();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onViewResumed();
    }

    private void initImageSlider() {
        for (int resId : featuresRedIds) {
            ;
            DefaultSliderView imageView = new DefaultSliderView(this);
            imageView
                    .image(resId)
                    .setScaleType(BaseSliderView.ScaleType.CenterCrop);
            featuresSliderLayout.addSlider(imageView);
        }
        featuresSliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
        featuresSliderLayout.setCustomIndicator(pagerIndicator);
        featuresSliderLayout.addOnPageChangeListener(this);
    }

    @Override
    protected void initPresenter() {
        presenter = new AppFeaturesPresenter();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_app_features;
    }

    @Override
    public void changePageDescription(int position) {
        featureDescriptionTextView.setText(featureDescriptionTexts[position]);
    }

    @Override
    public void animateOutTransition(int screenTransitionDuration) {
        featureDescriptionTextView.setVisibility(View.INVISIBLE);
        animateSignUpButtonOut();
        animateSliderLayout(screenTransitionDuration);
    }

    @Override
    public void launchAuthScreen() {
        startActivity(new Intent(this, AuthActivity.class));
        overridePendingTransition(0, 0);
    }

    @Override
    public void makeViewsVisible() {
        arcLayout.setVisibility(View.VISIBLE);
        signUpActionButton.setVisibility(View.VISIBLE);
        featureDescriptionTextView.setVisibility(View.VISIBLE);
    }

    private void animateSliderLayout(int screenTransitionDuration) {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.exit_slide_up);
        animation.setAnimationListener(new ViewAnimationListenerAdapter() {
            @Override
            public void onAnimationEnd(Animation animation) {
                arcLayout.setVisibility(View.GONE);
            }
        });
        animation.setDuration(screenTransitionDuration);
        arcLayout.startAnimation(animation);
    }

    private void animateSignUpButtonOut() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.exit_slide_down);
        animation.setAnimationListener(new ViewAnimationListenerAdapter() {
            @Override
            public void onAnimationEnd(Animation animation) {
                signUpActionButton.setVisibility(View.GONE);
            }
        });
        signUpActionButton.startAnimation(animation);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //no-op
    }

    @Override
    public void onPageSelected(int position) {
        presenter.onPageSelected(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //no-op
    }
}
