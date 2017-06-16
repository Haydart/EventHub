package pl.rmakowiecki.eventhub.ui.screen_user_profile;

import java.util.concurrent.TimeUnit;
import pl.rmakowiecki.eventhub.AvatarPickDialogFragment;
import pl.rmakowiecki.eventhub.ui.BasePresenter;
import rx.Observable;

class UserProfilePresenter extends BasePresenter<UserProfileView> {

    private static final int SHOW_BUTTON_RESULT_DELAY = 2000;
    private static final int LAUNCH_MAP_ACTIVITY_DELAY = 3500;
    private static final int BUTTON_ENABLE_DELAY = 5000;

    private boolean wasButtonClicked;

    @Override
    protected void onViewStarted(UserProfileView view) {
        super.onViewStarted(view);
        view.enableHomeButton();
        view.initRepository();
        view.changeToolbarTitles();
        view.displayInterestsList();
        view.loadUserImage();
        wasButtonClicked = false;
    }

    void onProfileSaveButtonClick() {
        if (!wasButtonClicked) {
            wasButtonClicked = true;
            view.saveProfile();
        }
    }

    void onPhotoOptionSelected(AvatarPickDialogFragment.AvatarPickDialogListener.AvatarSource photoSource) {
        if (photoSource == AvatarPickDialogFragment.AvatarPickDialogListener.AvatarSource.CAMERA)
            view.launchCameraAppIntent();
        else if (photoSource == AvatarPickDialogFragment.AvatarPickDialogListener.AvatarSource.GALLERY)
            view.launchGalleryAppIntent();
    }

    void onProfileSaveFailure() {
        delayButtonFailure();
        delayButtonEnable();
    }

    private void delayButtonFailure() {
        Observable.timer(SHOW_BUTTON_RESULT_DELAY, TimeUnit.MILLISECONDS)
                .compose(applySchedulers())
                .subscribe(ignored -> {
                    view.showFailureMessage();
                });
    }

    private void delayButtonEnable() {
        Observable.timer(BUTTON_ENABLE_DELAY, TimeUnit.MILLISECONDS)
                .compose(applySchedulers())
                .subscribe(ignored -> {
                    wasButtonClicked = false;
                });
    }

    void onProfileSaveSuccess() {
        delayButtonSuccess();
        delayMapLaunch();
    }

    private void delayButtonSuccess() {
        Observable.timer(SHOW_BUTTON_RESULT_DELAY, TimeUnit.MILLISECONDS)
                .compose(applySchedulers())
                .subscribe(ignored -> {
                    view.showProfileSaveSuccess();
                });
    }

    private void delayMapLaunch() {
        Observable.timer(LAUNCH_MAP_ACTIVITY_DELAY, TimeUnit.MILLISECONDS)
                .compose(applySchedulers())
                .subscribe(ignored -> {
                    view.launchMapAndFinish();
                });
    }

    void onProfileSaveProcessing() {
        view.showButtonProcessing();
    }

    void onChooseImageButtonClicked() {
        view.showPictureSelectDialog();
    }

    @Override
    public UserProfileView getNoOpView() {
        return NoOpUserProfileView.INSTANCE;
    }
}
