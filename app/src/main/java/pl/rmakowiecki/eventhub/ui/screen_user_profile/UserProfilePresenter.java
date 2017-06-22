package pl.rmakowiecki.eventhub.ui.screen_user_profile;

import java.util.concurrent.TimeUnit;
import pl.rmakowiecki.eventhub.model.local.User;
import pl.rmakowiecki.eventhub.repository.GenericQueryStatus;
import pl.rmakowiecki.eventhub.ui.AvatarPickDialogFragment;
import pl.rmakowiecki.eventhub.ui.BasePresenter;
import pl.rmakowiecki.eventhub.ui.screen_event_calendar.EventsRepository;
import pl.rmakowiecki.eventhub.util.UserAuthManager;
import pl.rmakowiecki.eventhub.util.UserManager;
import rx.Observable;

class UserProfilePresenter extends BasePresenter<UserProfileView> {

    private static final int SHOW_BUTTON_RESULT_DELAY = 2000;
    private static final int LAUNCH_MAP_ACTIVITY_DELAY = 3500;
    private static final int BUTTON_ENABLE_DELAY = 5000;
    private final int REQUIRED_SAVE_RESULT_COUNT = 2;

    private boolean wasButtonClicked;
    private UserProfileRepository repository = new UserProfileRepository();
    private EventsRepository eventsRepository = new EventsRepository();
    private UserManager userManager = new UserAuthManager();
    private int saveResultCount;
    private boolean isDifferentUser = false;
    private String userId = userManager.getCurrentUserId();

    @Override
    protected void onViewStarted(UserProfileView view) {
        super.onViewStarted(view);
        view.enableHomeButton();
        view.retrieveUserData();
        view.displayUserInfo(userManager, isDifferentUser);
        if (!isDifferentUser) {
            view.displayInterestsList();
        }
        else {
            loadEventsList();
        }
        handleUserImage();
        wasButtonClicked = false;
    }

    private void loadEventsList() {
        eventsRepository
                .queryForUserEvents(userId)
                .compose(applySchedulers())
                .subscribe(view::displayEventsList);
    }

    private void handleUserImage() {
        if (!isDifferentUser) {
            view.loadUserImage();
        }
        else {
            loadUserImageFromDatabase();
        }
    }

    void onProfileSaveButtonClick(User user) {
        if (!wasButtonClicked) {
            saveResultCount = 0;
            wasButtonClicked = true;
            saveProfile(user);
            view.showButtonProcessing();
        }
    }

    private void saveProfile(User user) {
        repository.add(user)
                .compose(applySchedulers())
                .subscribe(result -> onProfileSaveResult(result));
    }

    private void onProfileSaveResult(GenericQueryStatus result) {
        ++saveResultCount;
        if (result == GenericQueryStatus.STATUS_FAILURE) {
            showButtonFailureDelayed();
            enableButtonDelayed();
        } else if (result == GenericQueryStatus.STATUS_SUCCESS && saveResultCount >= REQUIRED_SAVE_RESULT_COUNT) {
            showButtonSuccessDelayed();
            launchMapScreenDelayed();
        }
    }

    void onPhotoOptionSelected(AvatarPickDialogFragment.AvatarPickDialogListener.AvatarSource photoSource) {
        if (photoSource == AvatarPickDialogFragment.AvatarPickDialogListener.AvatarSource.CAMERA)
            view.launchCameraAppIntent();
        else if (photoSource == AvatarPickDialogFragment.AvatarPickDialogListener.AvatarSource.GALLERY)
            view.launchGalleryAppIntent();
    }

    private void showButtonFailureDelayed() {
        Observable.timer(SHOW_BUTTON_RESULT_DELAY, TimeUnit.MILLISECONDS)
                .compose(applySchedulers())
                .subscribe(ignored -> view.showFailureMessage());
    }

    private void enableButtonDelayed() {
        Observable.timer(BUTTON_ENABLE_DELAY, TimeUnit.MILLISECONDS)
                .compose(applySchedulers())
                .subscribe(ignored -> wasButtonClicked = false);
    }

    private void showButtonSuccessDelayed() {
        Observable.timer(SHOW_BUTTON_RESULT_DELAY, TimeUnit.MILLISECONDS)
                .compose(applySchedulers())
                .subscribe(ignored -> view.showProfileSaveSuccess());
    }

    private void launchMapScreenDelayed() {
        Observable.timer(LAUNCH_MAP_ACTIVITY_DELAY, TimeUnit.MILLISECONDS)
                .compose(applySchedulers())
                .subscribe(ignored -> view.launchMapAndFinish());
    }

    public void loadUserImageFromDatabase() {
        UserProfileSpecification specification = new UserProfileSpecification(userId);
        repository
                .querySingle(specification)
                .compose(applySchedulers())
                .subscribe(this::onUserDataLoaded);
    }

    private void onUserDataLoaded(User userData) {
        if (userData != null)
            view.loadUserProfile(userData);
    }

    void onChooseImageButtonClicked() {
        view.showPictureSelectDialog();
    }

    @Override
    public UserProfileView getNoOpView() {
        return NoOpUserProfileView.INSTANCE;
    }

    public void onUserDataRetrieved(boolean isDifferentUser, String userId) {
        this.isDifferentUser = isDifferentUser;
        this.userId = userId;

        if (isDifferentUser)
            view.hideSettings();
    }
}
