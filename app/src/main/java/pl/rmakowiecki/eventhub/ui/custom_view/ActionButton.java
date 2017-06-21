package pl.rmakowiecki.eventhub.ui.custom_view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.wang.avi.AVLoadingIndicatorView;
import pl.rmakowiecki.eventhub.R;

public class ActionButton extends FrameLayout {
    private static final int CIRCULAR_REVEAL_DURATION = 500;
    private static final int CIRCULAR_REVEAL_RADIUS = 1024;
    private static final int CIRCULAR_REVEAL_DELAY = 1250;
    private static final int FAILURE_ICON_DISPLAYING_TIME = 800;
    private static final int SLIDE_DURATION = 300;
    private static final int SLIDE_DELAY = 300;
    private static final int SLIDE_IN_Y_DELTA = 500;
    private static final int SLIDE_OUT_Y_DELTA = -500;
    private static final int ERROR_DISPLAY_DURATION = 1500;
    private static final float DEFAULT_INACTIVE_ALPHA = .75f;
    public static final int START_NO_DELAY = 0;

    private int centerX, centerY;
    private int successColor;
    private int textColor;
    private int failureColor;
    private int defaultColor;
    private float inactiveAlpha;
    private String buttonActionDescription;
    private Drawable iconSuccess;
    private Drawable iconFailure;
    private LayoutInflater layoutInflater;
    private Context context;
    private boolean isEnabledInitially;
    private ButtonShape shape = ButtonShape.PILL;

    @BindView(R.id.frame_layout_button_success) FrameLayout successFrameLayout;
    @BindView(R.id.frame_layout_button_failure) FrameLayout failureFrameLayout;
    @BindView(R.id.text_view_button_action_desc) TextView buttonActionDescriptionTextView;
    @BindView(R.id.text_view_error_desc) TextView buttonErrorDescTextView;
    @BindView(R.id.progress_indicator) AVLoadingIndicatorView progressView;
    @BindView(R.id.icon_success) ImageView successImageView;
    @BindView(R.id.icon_failure) ImageView failureImageView;

    private enum ButtonShape {
        PILL,
        RECTANGLE
    }

    public ActionButton(Context context) {
        super(context);
        initView(context);
        bindViews();
        initAfterViewBinding();
    }

    public ActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        setStyledAttributes(context, attrs);
        bindViews();
        initAfterViewBinding();
    }

    private void initView(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.action_button, this, true);
    }

    private void setStyledAttributes(Context context, AttributeSet attrs) {
        TypedArray typedAttrArray = context.obtainStyledAttributes(attrs, R.styleable.ActionButton);
        textColor = typedAttrArray.getColor(R.styleable.ActionButton_text_color, Color.BLACK);
        successColor = typedAttrArray.getColor(R.styleable.ActionButton_success_color, Color.TRANSPARENT);
        defaultColor = typedAttrArray.getColor(R.styleable.ActionButton_default_color, Color.BLACK);
        failureColor = typedAttrArray.getColor(R.styleable.ActionButton_failure_color, Color.TRANSPARENT);
        iconSuccess = typedAttrArray.getDrawable(R.styleable.ActionButton_success_icon);
        iconFailure = typedAttrArray.getDrawable(R.styleable.ActionButton_failure_icon);
        inactiveAlpha = typedAttrArray.getFloat(R.styleable.ActionButton_inactive_button_alpha, DEFAULT_INACTIVE_ALPHA);
        isEnabledInitially = typedAttrArray.getBoolean(R.styleable.ActionButton_enabled, true);
        shape = (typedAttrArray.getInt(R.styleable.ActionButton_button_shape, 1)) == 0 ? ButtonShape.PILL : ButtonShape.RECTANGLE;
        buttonActionDescription = typedAttrArray.getString(R.styleable.ActionButton_text);
        typedAttrArray.recycle();
    }

    private void bindViews() {
        ButterKnife.bind(this);
    }

    private void initAfterViewBinding() {
        setEnabled(isEnabledInitially);
        setBackgroundDependingOnButtonShape(this);
        getBackground().setColorFilter(defaultColor, PorterDuff.Mode.ADD);
        buttonActionDescriptionTextView.setText(buttonActionDescription);
        buttonActionDescriptionTextView.setTextColor(textColor);
        successFrameLayout.getBackground().setColorFilter(successColor, PorterDuff.Mode.SRC_ATOP);
        failureFrameLayout.getBackground().setColorFilter(failureColor, PorterDuff.Mode.SRC_ATOP);
        successImageView.setImageDrawable(iconSuccess);
        iconSuccess.setColorFilter(textColor, PorterDuff.Mode.SRC_IN);
        failureImageView.setImageDrawable(iconFailure);
        iconFailure.setColorFilter(textColor, PorterDuff.Mode.SRC_IN);
        progressView.setIndicatorColor(textColor);
    }

    private void setBackgroundDependingOnButtonShape(ViewGroup viewGroup) {
        viewGroup.setBackgroundResource(shape == ButtonShape.PILL ?
                R.drawable.pill_action_button_ripple :
                R.drawable.rectangle_action_button_ripple
        );
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        calculateButtonCenter();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        isEnabledInitially = enabled;
        setClickable(enabled);
        setAlpha(isEnabledInitially ? 1f : inactiveAlpha);
    }

    public void setText(String text) {
        buttonActionDescriptionTextView.setText(text);
        buttonActionDescriptionTextView.setVisibility(VISIBLE);
    }

    public void showSuccess() {
        performSlideOutAnimation(progressView, START_NO_DELAY, () -> {
            progressView.setVisibility(INVISIBLE);
            successFrameLayout.setVisibility(VISIBLE);
        });
        performSlideInAnimation(successImageView, SLIDE_DELAY);
        performScaleUpAndFadeOutAnimation(successImageView, SLIDE_DELAY + SLIDE_DURATION, () -> successImageView.setVisibility(INVISIBLE));
    }

    public void showFailure(String message) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setLayoutsBackground();
            createAndStartCircularRevealAnimation(failureFrameLayout, CIRCULAR_REVEAL_DELAY);
        } else {
            performFadeInAnimation(failureFrameLayout);
            performSlideOutAnimation(progressView, CIRCULAR_REVEAL_DELAY, () -> progressView.setVisibility(INVISIBLE));
        }
        showErrorMessage(message, FAILURE_ICON_DISPLAYING_TIME);
    }

    private void setLayoutsBackground() {
        setBackgroundDependingOnButtonShape(failureFrameLayout);
        setBackgroundDependingOnButtonShape(successFrameLayout);
        failureFrameLayout.getBackground().setColorFilter(failureColor, PorterDuff.Mode.SRC_ATOP);
    }

    public void showProcessing() {
        setClickable(false);
        performSlideOutAnimation(buttonActionDescriptionTextView, 0, () -> buttonActionDescriptionTextView.setVisibility(INVISIBLE));
        performSlideInAnimation(progressView, SLIDE_DELAY);
    }

    public void setTextColor(int color) {
        buttonActionDescriptionTextView.setTextColor(color);
    }

    public void setLoaderColor(int color) {
        progressView.setIndicatorColor(color);
    }

    public void setColor(int color) {
        Drawable backgroundDrawable = ContextCompat.getDrawable(context, shape == ButtonShape.PILL ?
                R.drawable.pill_button_background :
                R.drawable.rectangle_button_background);
        backgroundDrawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        setBackground(backgroundDrawable);
        setEnabled(isEnabledInitially);
    }

    private void performSlideInAnimation(View view, int startOffset) {
        view.setVisibility(VISIBLE);
        Animation slideInAnimation = new TranslateAnimation(0, 0, SLIDE_IN_Y_DELTA, 0);
        slideInAnimation.setDuration(SLIDE_DURATION);
        slideInAnimation.setInterpolator(new LinearOutSlowInInterpolator());
        slideInAnimation.setStartOffset(startOffset);
        view.startAnimation(slideInAnimation);
    }

    private void performSlideOutAnimation(View view, int startOffset, Runnable onEndAction) {
        Animation slideOutAnimation = new TranslateAnimation(0, 0, 0, SLIDE_OUT_Y_DELTA);
        slideOutAnimation.setDuration(SLIDE_DURATION);
        slideOutAnimation.setStartOffset(startOffset);
        slideOutAnimation.setInterpolator(new FastOutLinearInInterpolator());
        slideOutAnimation.setAnimationListener(new AnimationListenerAdapter() {
            @Override
            public void onAnimationEnd(Animation animation) {
                onEndAction.run();
            }
        });
        view.startAnimation(slideOutAnimation);
    }

    private void performScaleUpAndFadeOutAnimation(View view, int startOffset, Runnable onEndAction) {
        Animation scaleUpFadeOutAnimation = AnimationUtils.loadAnimation(context, R.anim.scale_up_fade_out);
        scaleUpFadeOutAnimation.setStartOffset(startOffset);
        scaleUpFadeOutAnimation.setAnimationListener(new AnimationListenerAdapter() {
            @Override
            public void onAnimationEnd(Animation animation) {
                onEndAction.run();
            }
        });
        view.startAnimation(scaleUpFadeOutAnimation);
    }

    private void performFadeInAnimation(View view) {
        view.setVisibility(VISIBLE);
        Animation fadeInAnimation = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        view.startAnimation(fadeInAnimation);
    }

    private void performFadeOutAnimation(View view) {
        Animation fadeOutAnimation = AnimationUtils.loadAnimation(context, R.anim.fade_out);
        fadeOutAnimation.setAnimationListener(new AnimationListenerAdapter() {
            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(INVISIBLE);
            }
        });
        view.startAnimation(fadeOutAnimation);
    }

    private void resetButtonState(int buttonResetTime) {
        postDelayed(() -> {
            resetViewsVisibilityBeforeAnimation();
            ActionButton.this.performResetButtonState();
        }, buttonResetTime);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void createAndStartCircularRevealAnimation(View view, int startOffset) {
        final Animator circularRevealAnimation = ViewAnimationUtils.createCircularReveal(view,
                centerX, centerY, 0, CIRCULAR_REVEAL_RADIUS)
                .setDuration(CIRCULAR_REVEAL_DURATION);
        circularRevealAnimation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                view.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                resetButtonState(ERROR_DISPLAY_DURATION);
            }
        });
        circularRevealAnimation.setStartDelay(startOffset);
        circularRevealAnimation.start();
    }

    private void performResetButtonState() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            createAndStartCircularShrinkAnimation();
        } else {
            resetButtonStatePreLollipop();
        }
    }

    private void resetButtonStatePreLollipop() {
        postDelayed(() -> {
            resetViewsVisibilityAfterAnimation();
            setEnabled(true);
        }, CIRCULAR_REVEAL_DELAY);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void createAndStartCircularShrinkAnimation() {
        final Animator circularShrinkAnimation = ViewAnimationUtils.createCircularReveal(
                failureFrameLayout,
                centerX, centerY, CIRCULAR_REVEAL_RADIUS, 0);
        circularShrinkAnimation.setDuration(CIRCULAR_REVEAL_DURATION);
        circularShrinkAnimation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                resetViewsVisibilityAfterAnimation();
                setEnabled(true);
            }
        });
        circularShrinkAnimation.start();
    }

    private void resetViewsVisibilityAfterAnimation() {
        failureFrameLayout.setVisibility(INVISIBLE);
        buttonErrorDescTextView.setVisibility(INVISIBLE);
        failureImageView.setVisibility(VISIBLE);
    }

    private void resetViewsVisibilityBeforeAnimation() {
        progressView.setVisibility(INVISIBLE);
        buttonActionDescriptionTextView.setVisibility(VISIBLE);
    }

    private void showErrorMessage(String message, int failureIconDisplayingTime) {
        buttonErrorDescTextView.setText(message);
        performSlideInAnimation(buttonErrorDescTextView, failureIconDisplayingTime);
        performSlideOutAnimation(failureImageView, failureIconDisplayingTime, () -> failureImageView.setVisibility(INVISIBLE));
    }

    private void calculateButtonCenter() {
        centerX = getWidth() / 2;
        centerY = getHeight() / 2;
    }

    private class AnimationListenerAdapter implements Animation.AnimationListener {
        @Override
        public void onAnimationStart(Animation animation) {
            //no-op
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            //no-op
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
            //no-op
        }
    }
}
