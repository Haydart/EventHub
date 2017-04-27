package pl.rmakowiecki.eventhub.ui.screen_create_event;

import android.animation.ValueAnimator;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import butterknife.BindView;
import pl.rmakowiecki.eventhub.R;
import pl.rmakowiecki.eventhub.ui.BaseActivity;
import pl.rmakowiecki.eventhub.util.UnitConversionUtils;

public class EventCreationActivity extends BaseActivity<EventCreationPresenter> implements EventCreationView {

    public static final int APP_BAR_ANIMATION_DURATION = 300;

    @BindView(R.id.coordinator_layout) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.app_bar) AppBarLayout appBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupToolbar();
        animateAppbarPadding();
    }

    private void setupToolbar() {
        toolbar.setTitle(getString(R.string.title_event_creation));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void animateAppbarPadding() {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(getScreenHeightInPx(), 0);
        valueAnimator.setDuration(APP_BAR_ANIMATION_DURATION);
        valueAnimator.addUpdateListener(animation -> appBarLayout.setPadding(0, (Integer) animation.getAnimatedValue(), 0, 0));
        valueAnimator.start();
    }

    private int getScreenHeightInPx() {
        Configuration configuration = getResources().getConfiguration();
        int screenHeightDp = configuration.screenHeightDp;
        return (int) UnitConversionUtils.convertDpToPixel(this, screenHeightDp);
    }

    @Override
    protected void initPresenter() {
        presenter = new EventCreationPresenter();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_event_creation;
    }
}
