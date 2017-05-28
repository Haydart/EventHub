package pl.rmakowiecki.eventhub.ui.screen_auth;

import java.util.concurrent.TimeUnit;
import pl.rmakowiecki.eventhub.api.auth.AuthResponseInterceptor;
import pl.rmakowiecki.eventhub.api.auth.FirebaseAuthInteractor;
import pl.rmakowiecki.eventhub.api.auth.IAuthInteractor;
import pl.rmakowiecki.eventhub.model.remote.credentials.AuthCredentials;
import pl.rmakowiecki.eventhub.ui.BasePresenter;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

class AuthPresenter extends BasePresenter<AuthView> implements CredentialsValidator.CredentialsValidationCallback,
        AuthResponseInterceptor {

    private AuthPerspective authPerspective = AuthPerspective.LOGIN;
    private CredentialsValidator credentialsValidator;
    private Subscription credentialsChangesSubscription;
    private IAuthInteractor authInteractor;

    AuthPresenter() {
        credentialsValidator = new CredentialsValidator(this);
        authInteractor = new FirebaseAuthInteractor(this);
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
    public void onSuccess() {
        view.showSuccess();
        view.launchMainScreen();
    }

    @Override
    public void onNetworkConnectionError() {
        view.showNetworkConnectionError();
    }

    @Override
    public void onUnknownError() {
        view.showUnknownError();
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
    public AuthView getNoOpView() {
        return NoOpAuthView.INSTANCE;
    }
}
