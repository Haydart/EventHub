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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.OnClick;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import pl.rmakowiecki.eventhub.R;
import pl.rmakowiecki.eventhub.background.Constants;
import pl.rmakowiecki.eventhub.model.local.User;
import pl.rmakowiecki.eventhub.ui.AvatarPickDialogFragment;
import pl.rmakowiecki.eventhub.ui.BaseActivity;
import pl.rmakowiecki.eventhub.ui.custom_view.ActionButton;
import pl.rmakowiecki.eventhub.ui.screen_events_map.EventsMapActivity;
import pl.rmakowiecki.eventhub.ui.screen_preference_categories.PreferenceCategory;
import pl.rmakowiecki.eventhub.util.BitmapUtils;
import pl.rmakowiecki.eventhub.util.PreferencesManager;
import pl.rmakowiecki.eventhub.util.UserManager;

import static pl.rmakowiecki.eventhub.background.Constants.USER_PROFILE_EXTRA_IS_DIFFERENT_USER;
import static pl.rmakowiecki.eventhub.background.Constants.USER_PROFILE_EXTRA_USER_ID;

public class UserProfileActivity extends BaseActivity<UserProfilePresenter> implements UserProfileView, AvatarPickDialogFragment.AvatarPickDialogListener {

    @BindView(R.id.user_profile_appbar_layout) AppBarLayout appBarLayout;
    @BindView(R.id.user_profile_toolbar) Toolbar profileToolbar;
    @BindView(R.id.user_profile_toolbar_layout) CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.user_profile_image_view) ImageView userImageView;
    @BindView(R.id.save_user_profile_action_button) ActionButton saveProfileButton;
    @BindView(R.id.user_profile_preferences_recycler_view) RecyclerView recyclerView;
    @BindView(R.id.add_image_button) ImageView addImageView;
    @BindView(R.id.add_image_circle) CircleImageView addImageCircleView;

    private Bitmap pictureBitmap;
    private DialogFragment fragment;
    private RecyclerView.Adapter adapter;
    private PreferencesManager preferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(profileToolbar);
        preferencesManager = new PreferencesManager(this);
    }

    @Override
    public void retrieveUserData() {
        Bundle extras = getIntent().getExtras();
        boolean isDifferentUser = extras.getBoolean(USER_PROFILE_EXTRA_IS_DIFFERENT_USER);
        String userId = extras.getString(USER_PROFILE_EXTRA_USER_ID);
        presenter.onUserDataRetrieved(isDifferentUser, userId);
    }

    @Override
    public void hideSettings() {
        saveProfileButton.setVisibility(View.INVISIBLE);
        addImageView.setVisibility(View.INVISIBLE);
        addImageCircleView.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void displayUserInfo(UserManager userManager, boolean isDifferentUser) {
        profileToolbar.setTitle("");
        collapsingToolbarLayout.setTitle(!isDifferentUser ? userManager.getUserDisplayedName(this) : " ");

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    @Override
    public void loadUserImage() {
        Bitmap userImage = preferencesManager.getUserImage();
        setUserImage(userImage);
    }

    @Override
    public void loadUserProfile(User user) {
        collapsingToolbarLayout.setTitle(user.getName());
        byte[] pictureData = user.getPicture();
        if (pictureData != null) {
            Bitmap userImage = BitmapUtils.getBitmapFromBytes(pictureData);
            setUserImage(userImage);
        }
    }

    private void setUserImage(Bitmap userImage) {
        if (userImage != null) {
            pictureBitmap = userImage;
            userImageView.setImageBitmap(userImage);
        }
    }

    @Override
    public void displayInterestsList() {
        List<PreferenceCategory> displayList = preferencesManager.getInterestsDisplayList();
        if (!displayList.isEmpty()) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new UserProfilePreferencesAdapter(this, displayList);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    protected void initPresenter() {
        presenter = new UserProfilePresenter();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_user_profile;
    }

    @OnClick(R.id.add_image_button)
    public void onChooseImageButtonClicked() {
        presenter.onChooseImageButtonClicked();
    }

    @Override
    public void onDialogOptionChosen(AvatarSource avatarSource) {
        presenter.onPhotoOptionSelected(avatarSource);
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

    private void hideChoiceDialog() {
        getSupportFragmentManager()
                .beginTransaction()
                .remove(fragment)
                .commit();
    }

    public void applyTakenPhoto(Intent data, AvatarSource sourceType) {
        switch (sourceType) {
            case CAMERA:
                pictureBitmap = BitmapUtils.cropAndScaleBitmapFromCamera(data);
                break;
            case GALLERY:
                pictureBitmap = BitmapUtils.cropAndScaleBitmapFromGallery(data, this);
                break;
            default:
                return;
        }

        if (pictureBitmap != null) {
            userImageView.setImageBitmap(pictureBitmap); // TODO: 02.06.2017 Fix refreshing imageView when bitmap is already present
        }
    }

    @Override
    public void enableHomeButton() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @OnClick(R.id.save_user_profile_action_button)
    protected void saveProfileButtonClick() {
        User user = new User(collapsingToolbarLayout.getTitle().toString(), BitmapUtils.getBytesFromBitmap(pictureBitmap));
        presenter.onProfileSaveButtonClick(user);
        preferencesManager.saveUserDataLocally(user);
    }

    @Override
    public void showFailureMessage() {
        saveProfileButton.showFailure(getString(R.string.save_user_profile_failure_text));
    }

    @Override
    public void showButtonProcessing() {
        saveProfileButton.showProcessing();
    }

    @Override
    public void showProfileSaveSuccess() {
        saveProfileButton.showSuccess();
    }

    @Override
    public void launchMapAndFinish() {
        launchMapActivity();
        finish();
    }

    @Override
    public void showPictureSelectDialog() {
        fragment = new AvatarPickDialogFragment();
        fragment.show(getSupportFragmentManager(), Constants.DIALOG_FRAGMENT_TAG);
    }

    private void launchMapActivity() {
        Intent intent = new Intent(this, EventsMapActivity.class);
        startActivity(intent);
    }
}
