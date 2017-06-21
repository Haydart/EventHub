package pl.rmakowiecki.eventhub.ui.screen_auth;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.jenzz.noop.annotation.NoOp;
import java.util.List;

import pl.rmakowiecki.eventhub.model.local.GoogleUser;
import pl.rmakowiecki.eventhub.ui.BaseView;

@NoOp
interface AuthView extends BaseView {
    void openRegistrationForm();

    void openLoginForm();

    void performDefaultBackButtonPressAction();

    void displayLoginTextOnAuthButton();

    void displayRegisterTextOnAuthButton();

    void disableAuthButton();

    void enableAuthButton();

    void showInvalidEmailError();

    void showInvalidPasswordError();

    void showPasswordMatchingError();

    void hideAllErrors();

    void showSuccess();

    void launchPersonalizationScreen();

    void showNetworkConnectionError();

    void showUnknownError();

    void showRegisterUserCollisionError();

    void showRegisterCredentialsDiscardedError();

    void showLoginInvalidCredentialsError();

    void showButtonProcessing();

    void loginWithFacebookAuthentication(List<String> readPermissionsList);

    void showFacebookLoginError();

    void showGoogleLoginError();

    void loginWithGoogleAuthentication();

    void showFacebookLoginSuccess();

    void launchMainScreen();

    void showGoogleLoginSuccess();

    void saveUserGoogleData(GoogleUser signInAccount);
}
