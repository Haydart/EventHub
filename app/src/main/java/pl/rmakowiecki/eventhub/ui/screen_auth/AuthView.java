package pl.rmakowiecki.eventhub.ui.screen_auth;

import com.jenzz.noop.annotation.NoOp;
import pl.rmakowiecki.eventhub.ui.BaseView;

@NoOp
interface AuthView extends BaseView {
    void openRegistrationForm();

    void openLoginForm();

    void performDefaultBackButtonPressAction();

    void displayLoginTextOnAuthButton();

    void displayRegisterTextOnAuthButton();
}
