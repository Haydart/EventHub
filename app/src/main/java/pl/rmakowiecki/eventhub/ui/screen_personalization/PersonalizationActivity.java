package pl.rmakowiecki.eventhub.ui.screen_personalization;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.widget.ImageView;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import pl.rmakowiecki.eventhub.R;
import pl.rmakowiecki.eventhub.background.Constants;
import pl.rmakowiecki.eventhub.model.local.User;
import pl.rmakowiecki.eventhub.ui.AvatarPickDialogFragment;
import pl.rmakowiecki.eventhub.ui.BaseActivity;
import pl.rmakowiecki.eventhub.ui.custom_view.ActionButton;
import pl.rmakowiecki.eventhub.util.BitmapUtils;
import pl.rmakowiecki.eventhub.util.PreferencesManager;

public class PersonalizationActivity extends BaseActivity<PersonalizationPresenter> implements PersonalizationView, AvatarPickDialogFragment.AvatarPickDialogListener {

    @BindView(R.id.user_profile_image_view) ImageView userImageView;
    @BindView(R.id.personalization_action_button) ActionButton confirmButton;

    @BindString(R.string.button_text_general_fail) String dataSendingFailureText;

    private Bitmap pictureBitmap;
    private DialogFragment fragment;
    private PreferencesManager preferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferencesManager = new PreferencesManager(this);
    }

    @OnClick(R.id.add_image_button)
    public void onChooseImageButtonClicked() {
        presenter.onChooseImageButtonClicked();
    }

    @OnClick(R.id.personalization_action_button)
    public void onConfirmButtonClicked() {
        presenter.onConfirmButtonClicked();
    }

    @OnTextChanged(R.id.edit_text_displayed_name)
    public void onTextChanged(CharSequence newText) {
        presenter.onDisplayedNameTextChanged(newText.toString().trim());
    }

    @Override
    public void showPictureSelectDialog() {
        fragment = new AvatarPickDialogFragment();
        fragment.show(getSupportFragmentManager(), Constants.DIALOG_FRAGMENT_TAG);
    }

    @Override
    public void onDialogOptionChosen(AvatarPickDialogFragment.AvatarPickDialogListener.AvatarSource avatarSource) {
        presenter.onPhotoOptionSelected(avatarSource);
    }

    @Override
    public void enableConfirmButton() {
        confirmButton.setEnabled(true);
    }

    @Override
    public void disableConfirmButton() {
        confirmButton.setEnabled(false);
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
                applyTakenPhoto(data, AvatarPickDialogFragment.AvatarPickDialogListener.AvatarSource.CAMERA);
            } else if (requestCode == Constants.GALLERY_REQUEST_CODE) {
                applyTakenPhoto(data, AvatarPickDialogFragment.AvatarPickDialogListener.AvatarSource.GALLERY);
            }
        }
    }

    private void hideChoiceDialog() {
        getSupportFragmentManager()
                .beginTransaction()
                .remove(fragment)
                .commit();
    }

    public void applyTakenPhoto(Intent data, AvatarPickDialogFragment.AvatarPickDialogListener.AvatarSource sourceType) {
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
            userImageView.setImageBitmap(pictureBitmap);
            presenter.onPhotoSet(BitmapUtils.getBytesFromBitmap(pictureBitmap));
        }
    }

    @Override
    public void showPersonalizationUploadError() {
        confirmButton.showFailure(dataSendingFailureText);
    }

    @Override
    public void showPersonalizationUploadSuccess() {
        confirmButton.showSuccess();
    }

    @Override
    public void launchMainScreen() {
        finish();
    }

    @Override
    public void saveUserDataLocally(String displayedNameText, byte[] userPhoto) {
        preferencesManager.saveUserDataLocally(new User(
                displayedNameText, userPhoto
        ));
    }

    @Override
    protected void initPresenter() {
        presenter = new PersonalizationPresenter();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_personalization;
    }
}
