package pl.rmakowiecki.eventhub.ui.screen_auth;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.FrameLayout;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.jakewharton.rxbinding.widget.RxTextView;
import java.util.List;
import pl.rmakowiecki.eventhub.R;
import pl.rmakowiecki.eventhub.ui.BaseActivity;
import pl.rmakowiecki.eventhub.ui.custom_view.ActionButton;
import pl.rmakowiecki.eventhub.ui.screen_personalization.PersonalizationActivity;

public class AuthActivity extends BaseActivity<AuthPresenter> implements AuthView {

    @BindView(R.id.input_layout_email) TextInputLayout emailInputLayout;
    @BindView(R.id.input_layout_password) TextInputLayout passwordInputLayout;
    @BindView(R.id.input_layout_repeat_password) TextInputLayout passwordRepeatInputLayout;
    @BindView(R.id.edit_text_email) AppCompatEditText emailEditText;
    @BindView(R.id.edit_text_password) AppCompatEditText passwordEditText;
    @BindView(R.id.edit_text_repeat_password) AppCompatEditText passwordRepeatEditText;
    @BindView(R.id.login_bottom_layout) FrameLayout loginOptionsBottomLayout;
    @BindView(R.id.auth_action_button) ActionButton authActionButton;

    @BindString(R.string.button_text_sign_in) String signInButtonText;
    @BindString(R.string.button_text_sign_up) String signUpButtonText;
    @BindString(R.string.invalid_email_prompt) String invalidEmailErrorText;
    @BindString(R.string.invalid_password_prompt) String weakPasswordErrorText;
    @BindString(R.string.invalid_password_matching) String passwordsMismatchErrorText;

    @BindString(R.string.button_failure_network_connection) String networkConnectionErrorMessage;
    @BindString(R.string.button_failure_unknown_error) String unknownErrorMessage;
    @BindString(R.string.button_failure_invalid_credentials) String invalidCredentialsErrorMessage;
    @BindString(R.string.button_failure_email_taken) String emailTakenErrorMessage;
    @BindString(R.string.button_failure_credentials_discarded) String credentialsDiscardedErrorMessage;

    private CallbackManager facebookCallbackManager;

    @Override
    protected void onStart() {
        super.onStart();
        registerCredentialsChanges();
        setupFacebookLoginCallbacks();
    }

    private void setupFacebookLoginCallbacks() {
        facebookCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(facebookCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        presenter.onFacebookLoginSuccess(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        presenter.onFacebookLoginCancelled();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        presenter.onFacebookLoginFailed();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        facebookCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void initPresenter() {
        presenter = new AuthPresenter();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_auth;
    }

    private void registerCredentialsChanges() {
        presenter.onInputCredentialsChanged(
                RxTextView.textChanges(emailEditText),
                RxTextView.textChanges(passwordEditText),
                RxTextView.textChanges(passwordRepeatEditText)
        );
    }

    @OnTextChanged(R.id.edit_text_email)
    public void onEmailChanged() {
        presenter.onEmailChanged();
    }

    @OnTextChanged(R.id.edit_text_password)
    public void onPasswordChanged() {
        presenter.onPasswordChanged();
    }

    @OnTextChanged(R.id.edit_text_repeat_password)
    public void onPasswordRepeatChanged() {
        presenter.onPasswordRepeatChanged();
    }

    @OnClick(R.id.registration_form_button)
    public void onRegistrationFormButtonClicked() {
        presenter.onRegistrationFormButtonClicked();
    }

    @OnClick(R.id.auth_action_button)
    public void onAuthActionButtonClicked() {
        presenter.onAuthActionButtonClicked(emailEditText.getText().toString(), passwordEditText.getText().toString());
    }

    @Override
    public void openRegistrationForm() {
        passwordRepeatInputLayout.setVisibility(View.VISIBLE);
        loginOptionsBottomLayout.setVisibility(View.GONE);
    }

    @OnClick(R.id.image_view_login_fb)
    public void onFacebookLoginButtonClicked() {
        presenter.onFacebookLoginButtonClicked();
    }

    @Override
    public void loginWithFacebookAuthentication(List<String> readPermissionsList) {
        LoginManager.getInstance().logInWithReadPermissions(this, readPermissionsList);
    }

    @Override
    public void openLoginForm() {
        passwordRepeatInputLayout.setVisibility(View.GONE);
        loginOptionsBottomLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        presenter.onBackButtonPressed();
    }

    @Override
    public void performDefaultBackButtonPressAction() {
        super.onBackPressed();
    }

    @Override
    public void displayLoginTextOnAuthButton() {
        authActionButton.setText(signInButtonText);
    }

    @Override
    public void displayRegisterTextOnAuthButton() {
        authActionButton.setText(signUpButtonText);
    }

    @Override
    public void disableAuthButton() {
        authActionButton.setEnabled(false);
    }

    @Override
    public void enableAuthButton() {
        authActionButton.setEnabled(true);
    }

    @Override
    public void showInvalidEmailError() {
        emailInputLayout.setErrorEnabled(true);
        emailInputLayout.setError(invalidEmailErrorText);
    }

    @Override
    public void showInvalidPasswordError() {
        passwordInputLayout.setErrorEnabled(true);
        passwordInputLayout.setError(weakPasswordErrorText);
    }

    @Override
    public void showPasswordMatchingError() {
        passwordRepeatInputLayout.setErrorEnabled(true);
        passwordRepeatInputLayout.setError(passwordsMismatchErrorText);
    }

    @Override
    public void hideAllErrors() {
        emailInputLayout.setErrorEnabled(false);
        passwordInputLayout.setErrorEnabled(false);
        passwordRepeatInputLayout.setErrorEnabled(false);
    }

    @Override
    public void showButtonProcessing() {
        authActionButton.showProcessing();
    }

    @Override
    public void showSuccess() {
        authActionButton.showSuccess();
    }

    @Override
    public void launchPersonalizationScreen() {
        startActivity(new Intent(this, PersonalizationActivity.class));
        finish();
    }

    @Override
    public void showNetworkConnectionError() {
        authActionButton.showFailure(networkConnectionErrorMessage);
    }

    @Override
    public void showUnknownError() {
        authActionButton.showFailure(unknownErrorMessage);
    }

    @Override
    public void showRegisterUserCollisionError() {
        authActionButton.showFailure(emailTakenErrorMessage);
    }

    @Override
    public void showRegisterCredentialsDiscardedError() {
        authActionButton.showFailure(credentialsDiscardedErrorMessage);
    }

    @Override
    public void showLoginInvalidCredentialsError() {
        authActionButton.showFailure(invalidCredentialsErrorMessage);
    }
}
