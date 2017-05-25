package pl.rmakowiecki.eventhub.ui.screen_auth;

import pl.rmakowiecki.eventhub.ui.BasePresenter;
import rx.Observable;

class AuthPresenter extends BasePresenter<AuthView> {

    private AuthPerspective authPerspective = AuthPerspective.LOGIN;

    void onAuthActionButtonClicked() {
        // TODO: 25/05/2017 implement
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

    void onInputCredentialsChanged(Observable<CharSequence> emailObservable, Observable<CharSequence> passwordObservable,
            Observable<CharSequence> passwordRepeatPassword) {
        // TODO: 25/05/2017 implement
    }

    @Override
    public AuthView getNoOpView() {
        return NoOpAuthView.INSTANCE;
    }
}
