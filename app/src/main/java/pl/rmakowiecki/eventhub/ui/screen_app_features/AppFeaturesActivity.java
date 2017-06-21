package pl.rmakowiecki.eventhub.ui.screen_app_features;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.github.florent37.arclayout.ArcLayout;
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
    @BindString(R.string.button_text_sign_up) String joinButtonText;
    @BindString(R.string.button_text_next) String nextTabButtonText;
    @BindString(R.string.feature_slider_description_1) String feature1DescriptionText;
    @BindString(R.string.feature_slider_description_2) String feature2DescriptionText;
    @BindString(R.string.feature_slider_description_3) String feature3DescriptionText;
    @BindString(R.string.feature_slider_description_4) String feature4DescriptionText;

    private String[] featureDescriptionTexts;
    private int[] featuresRedIds = {
            R.drawable.feature_1,
            R.drawable.feature_2,
            R.drawable.feature_3,
            R.drawable.feature_4
    };

    @OnClick(R.id.sign_up_action_button)
    public void onSignUpButtonClicked() {
        presenter.onSignUpButtonClicked();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initImageSlider();
        featureDescriptionTexts = new String[] {
                feature1DescriptionText,
                feature2DescriptionText,
                feature3DescriptionText,
                feature4DescriptionText
        };
    }

    private void initImageSlider() {
        for (int resId : featuresRedIds) {
            DefaultSliderView imageView = new DefaultSliderView(this);
            imageView
                    .image(resId)
                    .setScaleType(BaseSliderView.ScaleType.CenterCrop);
            featuresSliderLayout.addSlider(imageView);
        }
        featuresSliderLayout.setCurrentPosition(0, false);
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
        featureDescriptionTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void animateInTransition(int screenTransitionDuration) {
        animateDescriptionTextIn(screenTransitionDuration);
        animateSignUpButtonIn();
        animateSliderLayoutIn(screenTransitionDuration);
    }

    private void animateDescriptionTextIn(int screenTransitionDuration) {
        featureDescriptionTextView.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        animation.setDuration(screenTransitionDuration);
        featureDescriptionTextView.startAnimation(animation);
    }

    private void animateSignUpButtonIn() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_from_down);
        signUpActionButton.startAnimation(animation);
    }

    private void animateSliderLayoutIn(int screenTransitionDuration) {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_from_up);
        animation.setDuration(screenTransitionDuration);
        arcLayout.startAnimation(animation);
    }

    @Override
    public void animateOutTransition(int screenTransitionDuration) {
        featureDescriptionTextView.setVisibility(View.INVISIBLE);
        animateSignUpButtonOut();
        animateSliderLayoutOut(screenTransitionDuration);
    }

    @Override
    public void launchAuthScreen() {
        startActivity(new Intent(this, AuthActivity.class));
        overridePendingTransition(0, 0);
        finish();
    }

    private void animateSliderLayoutOut(int screenTransitionDuration) {
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
    public void makeViewsVisible() {
        arcLayout.setVisibility(View.VISIBLE);
        signUpActionButton.setVisibility(View.VISIBLE);
        featureDescriptionTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showNextFeatureTab() {
        featuresSliderLayout.moveNextPosition(true);
    }

    @Override
    public void setButtonActionNextTab() {
        signUpActionButton.setText(nextTabButtonText);
    }

    @Override
    public void setButtonTextJoin() {
        signUpActionButton.setText(joinButtonText);
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
