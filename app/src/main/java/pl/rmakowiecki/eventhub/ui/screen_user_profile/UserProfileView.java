package pl.rmakowiecki.eventhub.ui.screen_user_profile;

import com.jenzz.noop.annotation.NoOp;

import java.util.List;

import pl.rmakowiecki.eventhub.model.local.Event;
import pl.rmakowiecki.eventhub.model.local.User;
import pl.rmakowiecki.eventhub.ui.BaseView;
import pl.rmakowiecki.eventhub.util.UserManager;

@NoOp
interface UserProfileView extends BaseView {

    void enableHomeButton();

    void loadUserProfile(User user);

    void displayInterestsList();

    void displayEventsList(List<Event> events);

    void launchCameraAppIntent();

    void launchGalleryAppIntent();

    void showFailureMessage();

    void showProfileSaveSuccess();

    void launchMapAndFinish();

    void showPictureSelectDialog();

    void showButtonProcessing();

    void displayUserInfo(UserManager userManager, boolean isDifferentUser);

    void loadUserImage();

    void retrieveUserData();

    void hideSettings();
}
