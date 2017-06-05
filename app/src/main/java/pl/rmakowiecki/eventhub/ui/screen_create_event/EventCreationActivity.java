package pl.rmakowiecki.eventhub.ui.screen_create_event;

import android.animation.ValueAnimator;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import java.util.Calendar;
import pl.rmakowiecki.eventhub.R;
import pl.rmakowiecki.eventhub.background.Constants;
import pl.rmakowiecki.eventhub.model.local.LocationCoordinates;
import pl.rmakowiecki.eventhub.ui.BaseActivity;
import pl.rmakowiecki.eventhub.util.UnitConversionUtils;

public class EventCreationActivity extends BaseActivity<EventCreationPresenter> implements EventCreationView {

    public static final int APP_BAR_ANIMATION_DURATION = 300;

    @BindView(R.id.coordinator_layout) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.app_bar) AppBarLayout appBarLayout;
    @BindView(R.id.picked_time_text_view) TextView timeTextView;
    @BindView(R.id.picked_date_text_view) TextView dateTextView;
    @BindView(R.id.event_address_text_view) TextView eventAddressTextView;
    @BindView(R.id.event_name_edit_text) EditText eventNameEditText;

    @OnClick(R.id.date_button)
    public void onDatePickerButtonClicked() {
        presenter.onDatePickerButtonClicked();
    }

    @Override
    public void showDatePickerView() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, monthOfYear, dayOfMonth) -> presenter.onEventDatePicked(year, monthOfYear, dayOfMonth),
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_YEAR));
        datePickerDialog.show();
    }

    @OnClick(R.id.time_button)
    public void onTimePickerButtonClicked() {
        presenter.onTimePickerButtonClicked();
    }

    @Override
    public void showTimePickerView() {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute) -> presenter.onEventTimePicked(hourOfDay, minute),
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
        timePickerDialog.show();
    }

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
    public void showEventPlaceAddress() {
        String placeAddress = (String) getIntent().getExtras().get(Constants.PLACE_ADDRESS_EXTRA);
        eventAddressTextView.setText(placeAddress != null ? placeAddress : "");
    }

    @Override
    public void showPickedDate(String date) {
        dateTextView.setText(date);
    }

    @Override
    public void showPickedTime(String time) {
        timeTextView.setText(time);
    }

    @OnClick(R.id.create_event_action_button)
    public void onButtonClicked() {
        presenter.onEventCreationButtonClicked(getEventCoordinates(), eventNameEditText.getText().toString(), eventAddressTextView.getText().toString());
    }

    private LocationCoordinates getEventCoordinates() {
        return (LocationCoordinates) getIntent().getExtras().get(Constants.LOCATION_DATA_EXTRA);
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
