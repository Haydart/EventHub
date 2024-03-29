package pl.rmakowiecki.eventhub.ui.screen_create_event;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import java.util.Calendar;
import java.util.List;
import pl.rmakowiecki.eventhub.R;
import pl.rmakowiecki.eventhub.background.Constants;
import pl.rmakowiecki.eventhub.model.local.LocationCoordinates;
import pl.rmakowiecki.eventhub.ui.AvatarPickDialogFragment;
import pl.rmakowiecki.eventhub.ui.BaseActivity;
import pl.rmakowiecki.eventhub.ui.custom_view.ActionButton;
import pl.rmakowiecki.eventhub.ui.screen_events_map.EventsMapActivity;
import pl.rmakowiecki.eventhub.ui.screen_preference_categories.PreferenceCategory;
import pl.rmakowiecki.eventhub.util.BitmapUtils;
import pl.rmakowiecki.eventhub.util.PreferencesManager;
import pl.rmakowiecki.eventhub.util.UnitConversionUtils;

public class EventCreationActivity extends BaseActivity<EventCreationPresenter> implements EventCreationView, AvatarPickDialogFragment.AvatarPickDialogListener,
        EventCategoryClickListener {

    public static final int APP_BAR_ANIMATION_DURATION = 300;
    public static final int GRID_SPAN_COUNT = 3;
    private static final String DIALOG_FRAGMENT_TAG = "dialog_fragment";
    @BindView(R.id.coordinator_layout) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.app_bar) AppBarLayout appBarLayout;
    @BindView(R.id.event_address_edit_text) TextInputEditText eventAddressEditText;
    @BindView(R.id.event_name_edit_text) TextInputEditText eventNameEditText;
    @BindView(R.id.event_description_edit_text) TextInputEditText eventDescriptionEditText;
    @BindView(R.id.event_image_view) ImageView eventImageView;
    @BindView(R.id.event_categories_recycler_view) RecyclerView eventCategoriesRecyclerView;
    @BindView(R.id.event_date_button) ActionButton eventDateActionButton;
    @BindView(R.id.event_time_button) ActionButton eventTimeActionButton;
    @BindView(R.id.create_event_action_button) ActionButton eventCreateActionButton;
    @BindString(R.string.save_user_profile_failure_text) String saveFailureText;

    private DialogFragment fragment;
    private Bitmap eventAvatarBitmap;
    private PreferencesManager preferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupToolbar();
        setupEventCategoryRecyclerView();
        animateAppbarPadding();
        preferencesManager = new PreferencesManager(this);
    }

    @SuppressWarnings("ConstantConditions")
    private void setupToolbar() {
        toolbar.setTitle(getString(R.string.title_event_creation));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void setupEventCategoryRecyclerView() {
        eventCategoriesRecyclerView.setLayoutManager(new GridLayoutManager(this, GRID_SPAN_COUNT));
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
        eventAddressEditText.setText(placeAddress != null ? placeAddress : "");
    }

    @OnClick(R.id.event_image_view)
    public void onEventImageClicked() {
        presenter.onEventImageClicked();
    }

    @Override
    public void showAvatarSelectDialog() {
        fragment = new AvatarPickDialogFragment();
        fragment.show(getSupportFragmentManager(), DIALOG_FRAGMENT_TAG);
    }

    @Override
    public void onDialogOptionChosen(AvatarSource avatarSource) {
        presenter.onPhotoOptionSelected(avatarSource);
    }

    @Override
    public void showPickedDate(String date) {
        eventDateActionButton.setText(date);
    }

    @Override
    public void showPickedTime(String time) {
        eventTimeActionButton.setText(time);
    }

    @Override
    public void showCategoriesList(List<PreferenceCategory> categoryList) {
        eventCategoriesRecyclerView.setAdapter(new EventCategoryAdapter(this, categoryList, this));
    }

    @Override
    public void onCategoryClicked(int position, PreferenceCategory category) {
        presenter.onEventCategoryClicked(position, category);
    }

    @Override
    public void displayEventSubcategoryPicker(int position, PreferenceCategory fullCategory, PreferenceCategory previouslyPickedCategory) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(fullCategory.getTitle());

        List<String> subcategories = fullCategory.getChildList();
        boolean[] checkedItems = new boolean[subcategories.size()];

        if (previouslyPickedCategory != null) {
            int previouslyPickedSubcategoriesConvertedCount = 0;
            for (int i = 0; i < fullCategory.getChildList().size() &&
                    previouslyPickedSubcategoriesConvertedCount < previouslyPickedCategory.getChildList().size(); i++) {
                if (fullCategory.getChildList().get(i).equals(previouslyPickedCategory.getChildList().get(previouslyPickedSubcategoriesConvertedCount))) {
                    previouslyPickedSubcategoriesConvertedCount++;
                    checkedItems[i] = true;
                }
            }
        }

        builder.setMultiChoiceItems(subcategories.toArray(new String[subcategories.size()]), checkedItems, (dialog, which, isChecked) -> {
            // user checked or unchecked a box
        });

        builder.setPositiveButton(android.R.string.ok, (dialog, which) -> presenter.onSubcategoriesPicked(fullCategory, checkedItems));
        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    @Override
    public void showFailureMessage() {
        eventCreateActionButton.showFailure(saveFailureText);
    }

    @Override
    public void showButtonProcessing() {
        eventCreateActionButton.showProcessing();
    }

    @Override
    public void showProfileSaveSuccess() {
        eventCreateActionButton.showSuccess();
    }

    @Override
    public void launchMapAndFinish() {
        launchMapActivity();
        finish();
    }

    private void launchMapActivity() {
        Intent intent = new Intent(this, EventsMapActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.event_date_button)
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

    @OnClick(R.id.event_time_button)
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        hideChoiceDialog();

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.CAMERA_REQUEST_CODE) {
                applyTakenPhoto(data, AvatarSource.CAMERA);
            } else if (requestCode == Constants.GALLERY_REQUEST_CODE) {
                applyTakenPhoto(data, AvatarSource.GALLERY);
            }
        }
    }

    @Override
    public void launchCameraAppIntent() {
        Intent photoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (photoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(photoIntent, Constants.CAMERA_REQUEST_CODE);
        }
    }

    @Override
    public void launchGalleryAppIntent() {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType(Constants.DEVICE_IMAGES_MIME);

        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType(Constants.DEVICE_IMAGES_MIME);

        Intent chooserIntent = Intent.createChooser(getIntent, getString(R.string.gallery_option_window_text));
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] { pickIntent });

        startActivityForResult(chooserIntent, Constants.GALLERY_REQUEST_CODE);
    }

    private void hideChoiceDialog() {
        getSupportFragmentManager()
                .beginTransaction()
                .remove(fragment)
                .commit();
    }

    public void applyTakenPhoto(Intent data, AvatarSource sourceType) {
        switch (sourceType) {
            case CAMERA:
                eventAvatarBitmap = BitmapUtils.cropAndScaleBitmapFromCamera(data);
                break;
            case GALLERY:
                eventAvatarBitmap = BitmapUtils.cropAndScaleBitmapFromGallery(data, this);
                break;
            default:
                return;
        }

        if (eventAvatarBitmap != null) {
            eventImageView.setVisibility(View.VISIBLE);
            eventImageView.setImageBitmap(eventAvatarBitmap); // TODO: 02.06.2017 Fix refreshing imageView when bitmap is already present
        }
    }

    @OnClick(R.id.create_event_action_button)
    public void onButtonClicked() {
        presenter.onEventCreationButtonClicked(
                getEventCoordinates(),
                eventNameEditText.getText().toString(),
                eventDescriptionEditText.getText().toString(),
                eventAddressEditText.getText().toString(),
                preferencesManager.getUserName(),
                eventAvatarBitmap != null ? BitmapUtils.getBytesFromBitmap(eventAvatarBitmap) : new byte[]{}
        );
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
