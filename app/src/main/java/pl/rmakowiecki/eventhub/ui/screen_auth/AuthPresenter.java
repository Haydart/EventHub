package pl.rmakowiecki.eventhub.ui.screen_auth;

import com.facebook.AccessToken;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import pl.rmakowiecki.eventhub.api.auth.AuthResponseInterceptor;
import pl.rmakowiecki.eventhub.api.auth.FirebaseAuthInteractor;
import pl.rmakowiecki.eventhub.api.auth.IAuthInteractor;
import pl.rmakowiecki.eventhub.model.local.GoogleUser;
import pl.rmakowiecki.eventhub.model.local.Interest;
import pl.rmakowiecki.eventhub.model.local.User;
import pl.rmakowiecki.eventhub.model.remote.credentials.AuthCredentials;
import pl.rmakowiecki.eventhub.ui.BasePresenter;
import pl.rmakowiecki.eventhub.ui.screen_preference_categories.PreferenceInterestRepository;
import pl.rmakowiecki.eventhub.ui.screen_preference_categories.PreferenceInterestSpecification;
import pl.rmakowiecki.eventhub.ui.screen_user_profile.UserProfileRepository;
import pl.rmakowiecki.eventhub.util.PreferencesManager;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

class AuthPresenter extends BasePresenter<AuthView> implements CredentialsValidator.CredentialsValidationCallback,
        AuthResponseInterceptor {

    private static final int SCREEN_LAUNCH_DELAY = 1000;
    private AuthPerspective authPerspective = AuthPerspective.LOGIN;
    private CredentialsValidator credentialsValidator;
    private Subscription credentialsChangesSubscription;
    private IAuthInteractor authInteractor;
    private List<String> readPermissionsList = Arrays.asList("email", "public_profile", "user_friends");
    private PreferenceInterestRepository preferenceInterestRepository;
    private PreferencesManager preferencesManager;
    private UserProfileRepository userProfileRepository;

    AuthPresenter(PreferencesManager prefManager) {
        preferencesManager = prefManager;
        credentialsValidator = new CredentialsValidator(this);
        authInteractor = new FirebaseAuthInteractor(this);
        preferenceInterestRepository = new PreferenceInterestRepository();
        userProfileRepository = new UserProfileRepository();
    }

    void onEmailChanged() {
        view.disableAuthButton();
        view.hideAllErrors();
    }

    void onPasswordChanged() {
        view.disableAuthButton();
        view.hideAllErrors();
    }

    void onPasswordRepeatChanged() {
        view.disableAuthButton();
        view.hideAllErrors();
    }

    void onInputCredentialsChanged(Observable<CharSequence> emailObservable, Observable<CharSequence> passwordObservable, Observable<CharSequence> passwordRepeatObservable) {
        validateCredentialsWhenUserStopsTyping(
                emailObservable.debounce(1, TimeUnit.SECONDS),
                passwordObservable.debounce(1, TimeUnit.SECONDS),
                passwordRepeatObservable.debounce(1, TimeUnit.SECONDS));
    }

    private void validateCredentialsWhenUserStopsTyping(Observable<CharSequence> emailObservable, Observable<CharSequence> passwordObservable,
            Observable<CharSequence> passwordRepeatObservable) {
        credentialsChangesSubscription = Observable.combineLatest(emailObservable, passwordObservable, passwordRepeatObservable,
                (email, password, passwordRepeat) -> new AuthCredentials(email.toString(), password.toString(), passwordRepeat.toString()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(authCredentials -> credentialsValidator.validateCredentials(authCredentials, authPerspective));
    }

    void onRegistrationFormButtonClicked() {
        authPerspective = AuthPerspective.REGISTER;
        view.openRegistrationForm();
        view.displayRegisterTextOnAuthButton();
    }

    void onAuthActionButtonClicked(String email, String password) {
        view.showButtonProcessing();
        if (authPerspective == AuthPerspective.LOGIN) {
            loginUser(email, password);
        } else {
            registerUser(email, password);
        }
    }

    private void loginUser(String email, String password) {
        authInteractor.loginUserWithEmail(email, password);
    }

    private void registerUser(String email, String password) {
        authInteractor.registerUserWithEmail(email, password);
    }

    @Override
    public void onAllCredentialsValid() {
        view.enableAuthButton();
    }

    @Override
    public void onEmailInvalid() {
        view.showInvalidEmailError();
    }

    @Override
    public void onPasswordInvalid() {
        view.showInvalidPasswordError();
    }

    @Override
    public void onPasswordRepeatInvalid() {
        view.showPasswordMatchingError();
    }

    @Override
    public void onSuccess(AuthPerspective authPerspective) {
        if (authPerspective == AuthPerspective.REGISTER)
            saveInterests();
        else
            loadInterests();
    }

    @Override
    public void onInvalidCredentials() {
        view.showLoginInvalidCredentialsError();
    }

    @Override
    public void onEmailAlreadyTaken() {
        view.showRegisterUserCollisionError();
    }

    @Override
    public void onCredentialsDiscarded() {
        view.showRegisterCredentialsDiscardedError();
    }

    @Override
    public void onNetworkConnectionError() {
        view.showNetworkConnectionError();
    }

    @Override
    public void onUnknownError() {
        view.showUnknownError();
    }

    void onFacebookLoginButtonClicked() {
        view.loginWithFacebookAuthentication(readPermissionsList);
    }

    void onFacebookLoginSuccess(AccessToken accessToken) {
        authInteractor.loginWithFacebook(accessToken);
    }

    void onFacebookLoginCancelled() {
        view.showFacebookLoginError();
    }

    void onFacebookLoginFailed() {
        view.showFacebookLoginError();
    }

    void onBackButtonPressed() {
        if (authPerspective == AuthPerspective.LOGIN) {
            view.performDefaultBackButtonPressAction();
        } else {
            authPerspective = AuthPerspective.LOGIN;
            view.openLoginForm();
            view.displayLoginTextOnAuthButton();
        }
    }

    @Override
    public void onFacebookLoginSuccess() {
        view.showFacebookLoginSuccess();
        launchMainScreenDelayed();
    }

    @Override
    public void onFacebookLoginError() {
        view.showFacebookLoginError();
    }

    void onGoogleLoginButtonClicked() {
        view.loginWithGoogleAuthentication();
    }

    @Override
    public void onGoogleLoginSuccess(String accountIdToken, GoogleUser user) {
        authInteractor.loginWithGoogle(accountIdToken, user);
    }

    @Override
    public void onGoogleLoginError() {
        view.showGoogleLoginError();
    }

    @Override
    public void onDatabaseGoogleLoginSuccess(GoogleUser user) {
        view.saveUserGoogleData(user);
        view.showGoogleLoginSuccess();
        launchMainScreenDelayed();
    }

    private void saveInterests() {
        preferenceInterestRepository.savePreferences(preferencesManager.getUserInterestsMap());
        view.showSuccess();
        launchPersonalizationScreenDelayed();
    }

    private void loadInterests() {
        preferenceInterestRepository
                .query(new PreferenceInterestSpecification() {})
                .compose(applySchedulers())
                .subscribe(this::onInterestsLoaded);
    }

    private void onInterestsLoaded(List<Interest> interestsList) {
        preferencesManager.saveInterests(interestsList);
        view.showSuccess();
        launchPersonalizationScreenDelayed();
    }

    void onUserDataRetrieved(User user) {
        userProfileRepository.add(user);
    }

    private void launchPersonalizationScreenDelayed() {
        Observable.timer(SCREEN_LAUNCH_DELAY, TimeUnit.MILLISECONDS)
                .compose(applySchedulers())
                .subscribe((ignored) -> view.launchPersonalizationScreen());
    }

    private void launchMainScreenDelayed() {
        Observable.timer(SCREEN_LAUNCH_DELAY, TimeUnit.MILLISECONDS)
                .compose(applySchedulers())
                .subscribe((ignored) -> view.launchMainScreen());
    }

    @Override
    public AuthView getNoOpView() {
        return NoOpAuthView.INSTANCE;
    }
}
