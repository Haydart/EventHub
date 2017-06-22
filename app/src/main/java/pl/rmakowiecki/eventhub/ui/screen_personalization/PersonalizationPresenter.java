package pl.rmakowiecki.eventhub.ui.screen_personalization;

import java.util.concurrent.TimeUnit;
import pl.rmakowiecki.eventhub.model.local.User;
import pl.rmakowiecki.eventhub.repository.GenericQueryStatus;
import pl.rmakowiecki.eventhub.ui.AvatarPickDialogFragment;
import pl.rmakowiecki.eventhub.ui.BasePresenter;
import pl.rmakowiecki.eventhub.ui.screen_user_profile.UserProfileRepository;
import pl.rmakowiecki.eventhub.util.TextUtils;
import rx.Observable;

class PersonalizationPresenter extends BasePresenter<PersonalizationView> {

    private static final int SCREEN_LAUNCH_DELAY_MILLIS = 600;
    private static final int SAFETY_DELAY = 100;
    private String displayedNameText;
    private byte[] userPhoto;
    private UserProfileRepository userRepository;

    PersonalizationPresenter() {
        userRepository = new UserProfileRepository();
    }

    void onChooseImageButtonClicked() {
        view.showPictureSelectDialog();
    }

    void onPhotoOptionSelected(AvatarPickDialogFragment.AvatarPickDialogListener.AvatarSource photoSource) {
        if (photoSource == AvatarPickDialogFragment.AvatarPickDialogListener.AvatarSource.CAMERA) {
            view.launchCameraAppIntent();
        } else if (photoSource == AvatarPickDialogFragment.AvatarPickDialogListener.AvatarSource.GALLERY) {
            view.launchGalleryAppIntent();
        }
    }

    void onConfirmButtonClicked() {
        userRepository
                .add(new User(displayedNameText, userPhoto))
                .compose(applySchedulers())
                .subscribe(this::handleResponse);
        view.saveUserDataLocally(displayedNameText, userPhoto);
    }

    private void handleResponse(GenericQueryStatus genericQueryStatus) {
        if (genericQueryStatus == GenericQueryStatus.STATUS_SUCCESS) {
            view.showPersonalizationUploadSuccess();
            launchMainScreenDelayed();
        } else {
            view.showPersonalizationUploadError();
        }
    }

    private void launchMainScreenDelayed() {
        Observable
                .timer(SCREEN_LAUNCH_DELAY_MILLIS, TimeUnit.MILLISECONDS)
                .compose(applySchedulers())
                .subscribe(ignored -> view.launchMainScreen());
    }

    void onPhotoSet(byte[] photo) {
        userPhoto = photo;
        manageButtonState();
    }

    void onDisplayedNameTextChanged(String newText) {
        displayedNameText = newText;
        manageButtonState();
    }

    private void manageButtonState() {
        if (userPhoto != null && userPhoto.length > 0 && TextUtils.isNotEmpty(displayedNameText)) {
            enableConfirmButtonDelayed();
        } else {
            view.disableConfirmButton();
        }
    }

    private void enableConfirmButtonDelayed() {
        Observable
                .timer(SAFETY_DELAY, TimeUnit.MILLISECONDS)
                .compose(applySchedulers())
                .subscribe(ignored -> view.enableConfirmButton());
    }

    public PersonalizationView getNoOpView() {
        return NoOpPersonalizationView.INSTANCE;
    }
}
