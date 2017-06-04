package pl.rmakowiecki.eventhub.ui.screen_user_profile;

import com.jenzz.noop.annotation.NoOp;

import pl.rmakowiecki.eventhub.ui.BaseView;

@NoOp
public interface UserProfileView extends BaseView {

    void enableHomeButton();

    void saveProfile();

    void displayInterestsList();

    void launchCameraAppIntent();

    void launchGalleryAppIntent();

    void showFailureMessage();

    void showProfileSaveSuccess();

    void launchMapAndFinish();

    void showPictureSelectFragment();

    void initRepository();

    void showButtonProcessing();

    void changeToolbarTitles();

    void loadUserImage();
}
