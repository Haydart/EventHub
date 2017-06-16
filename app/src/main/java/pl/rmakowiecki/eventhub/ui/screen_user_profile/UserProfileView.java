package pl.rmakowiecki.eventhub.ui.screen_user_profile;

import com.jenzz.noop.annotation.NoOp;
import pl.rmakowiecki.eventhub.ui.BaseView;
import pl.rmakowiecki.eventhub.util.UserManager;

@NoOp
interface UserProfileView extends BaseView {

    void enableHomeButton();

    void displayInterestsList();

    void launchCameraAppIntent();

    void launchGalleryAppIntent();

    void showFailureMessage();

    void showProfileSaveSuccess();

    void launchMapAndFinish();

    void showPictureSelectDialog();

    void showButtonProcessing();

    void displayUserInfo(UserManager userDisplayedName);

    void loadUserImage();
}
