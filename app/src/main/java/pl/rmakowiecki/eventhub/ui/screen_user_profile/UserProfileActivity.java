package pl.rmakowiecki.eventhub.ui.screen_user_profile;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;
import pl.rmakowiecki.eventhub.R;
import pl.rmakowiecki.eventhub.model.local.UserProfile;
import pl.rmakowiecki.eventhub.ui.BaseActivity;
import pl.rmakowiecki.eventhub.ui.custom_view.ActionButton;
import pl.rmakowiecki.eventhub.ui.screen_events_map.EventsMapActivity;
import pl.rmakowiecki.eventhub.ui.screen_preference_categories.PreferenceCategory;
import pl.rmakowiecki.eventhub.util.BitmapUtils;
import pl.rmakowiecki.eventhub.util.LocaleUtils;
import pl.rmakowiecki.eventhub.util.PreferencesManager;

public class UserProfileActivity extends BaseActivity<UserProfilePresenter> implements UserProfileView {

    public static final int CAMERA_REQUEST_CODE = 1;
    private static final String DIALOG_FRAGMENT_TAG = "dialog_fragment";
    private static final String DEVICE_IMAGES_MIME = "image/*";
    private static final int GALLERY_REQUEST_CODE = 2;
    private static final int PHOTO_SOURCE_CAMERA = 1;
    private static final int PHOTO_SOURCE_GALLERY = 2;

    @BindView(R.id.user_profile_appbar_layout) AppBarLayout appBarLayout;
    @BindView(R.id.user_profile_toolbar) Toolbar profileToolbar;
    @BindView(R.id.user_profile_toolbar_layout) CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.user_profile_image_view) ImageView imageView;
    @BindView(R.id.save_user_profile_action_button) ActionButton saveProfileButton;
    @BindView(R.id.user_profile_preferences_recycler_view) RecyclerView recyclerView;

    private UserProfileRepository repository;
    private Bitmap pictureBitmap;
    private DialogFragment fragment;
    private RecyclerView.Adapter adapter;
    private PreferencesManager preferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(profileToolbar);
    }

    @Override
    public void initRepository() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        repository = new UserProfileRepository(presenter, user);
    }

    @Override
    public void initManagers() {
        preferencesManager = new PreferencesManager(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void changeToolbarTitles() {
        profileToolbar.setTitle("");
        collapsingToolbarLayout.setTitle("Imie Nazwisko");

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    public List<PreferenceCategory> getInterestsList() {
        List<PreferenceCategory> categoryList = preferencesManager.getPreferenceCategoryList();
        return buildInterestsDisplayList(categoryList);
    }

    public List<PreferenceCategory> buildInterestsDisplayList(List<PreferenceCategory> categoryList) {
        List<PreferenceCategory> displayList = new ArrayList<>();

        for (PreferenceCategory category : categoryList) {
            String categoryName = category.getTitle();
            Set<String> interests = preferencesManager.getInterests(categoryName);

            if (!interests.isEmpty()) {
                LocaleUtils utils = new LocaleUtils();
                if (utils.hasLocale()) {
                    categoryName = preferencesManager.getNameOrLocaleName(utils.getLocaleString(), category.getTitle(), category.getTitle());
                    List<String> translatedSubcategories = new ArrayList<>();
                    for (String subCategory : interests) {
                        translatedSubcategories.add(preferencesManager.getNameOrLocaleName(utils.getLocaleString(), category.getTitle(), subCategory));
                    }
                    displayList.add(new PreferenceCategory(categoryName, "", translatedSubcategories));
                }
                else {
                    List<String> subcategories = new ArrayList<>();
                    subcategories.addAll(interests);
                    displayList.add(new PreferenceCategory(categoryName, "", subcategories));
                }
            }
        }

        return displayList;
    }

    @Override
    public void displayInterestsList() {
        List<PreferenceCategory> displayList = getInterestsList();
        if (!displayList.isEmpty()) {
            adapter = new UserProfilePreferencesAdapter(getBaseContext(), displayList);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    protected void initPresenter() {
        presenter = new UserProfilePresenter();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.user_profile_activity;
    }

    @OnClick(R.id.add_image_button)
    public void onChooseImageButtonClicked() {
        presenter.onChooseImageButtonClicked();
    }

    public void onDialogFragmentButtonClick(int photoSource) {
        presenter.onPhotoOptionSelected(photoSource);
    }

    @Override
    public void launchCameraAppIntent() {
        Intent photoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (photoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(photoIntent, CAMERA_REQUEST_CODE);
        }
    }

    @Override
    public void launchGalleryAppIntent() {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType(DEVICE_IMAGES_MIME);

        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType(DEVICE_IMAGES_MIME);

        Intent chooserIntent = Intent.createChooser(getIntent, getString(R.string.gallery_option_window_text));
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] { pickIntent });

        startActivityForResult(chooserIntent, GALLERY_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        hideChoiceDialog();

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAMERA_REQUEST_CODE) {
                applyTakenPhoto(data, PHOTO_SOURCE_CAMERA);
            } else if (requestCode == GALLERY_REQUEST_CODE) {
                applyTakenPhoto(data, PHOTO_SOURCE_GALLERY);
            }
        }
    }

    private void hideChoiceDialog() {
        getSupportFragmentManager()
                .beginTransaction()
                .remove(fragment)
                .commit();
    }

    public void applyTakenPhoto(Intent data, int sourceType) {
        switch (sourceType) {
            case PHOTO_SOURCE_CAMERA:
                pictureBitmap = BitmapUtils.cropAndScaleBitmapFromCamera(data);
                break;
            case PHOTO_SOURCE_GALLERY:
                pictureBitmap = BitmapUtils.cropAndScaleBitmapFromGallery(data, this);
                break;
            default:
                return;
        }

        if (pictureBitmap != null) {
            imageView.setImageBitmap(pictureBitmap);
        }
    }

    @Override
    public void enableHomeButton() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @OnClick(R.id.save_user_profile_action_button)
    protected void saveProfileButtonClick() {
        presenter.onProfileSaveButtonClick();
    }

    @Override
    public void showFailureMessage() {
        saveProfileButton.showFailure(getString(R.string.save_user_profile_failure_text));
    }

    @Override
    public void showButtonProcessing() {
        saveProfileButton.showProcessing();
    }

    private byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        return outputStream.toByteArray();
    }

    // TODO: 01.05.2017 Rework this method when more save-able options are implemented
    public void saveProfile() {
        if (pictureBitmap == null) {
            saveProfileButton.showProcessing();
            presenter.onProfileSaveSuccess();
            return;
        }

        byte[] data = getBytesFromBitmap(pictureBitmap);
        UserProfile profile = new UserProfile(data);
        repository.add(profile);
    }

    @Override
    public void launchMapAndFinish() {
        launchMapActivity();
        finish();
    }

    @Override
    public void showPictureSelectFragment() {
        fragment = new UserPictureRetrievalDialogFragment();
        fragment.show(getSupportFragmentManager(), DIALOG_FRAGMENT_TAG);
    }

    private void launchMapActivity() {
        Intent intent = new Intent(this, EventsMapActivity.class);
        startActivity(intent);
    }

    @Override
    public void showProfileSaveSuccess() {
        saveProfileButton.showSuccess();
    }
}
