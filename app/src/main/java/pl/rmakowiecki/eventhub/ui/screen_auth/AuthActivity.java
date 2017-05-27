package pl.rmakowiecki.eventhub.ui.screen_auth;

import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.FrameLayout;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import com.jakewharton.rxbinding.widget.RxTextView;
import pl.rmakowiecki.eventhub.R;
import pl.rmakowiecki.eventhub.ui.BaseActivity;
import pl.rmakowiecki.eventhub.ui.custom_view.ActionButton;

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


    @Override
    protected void onStart() {
        super.onStart();
        registerCredentialsChanges();
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
    public void showSuccess() {
        // TODO: 27/05/2017 implement
    }

    @Override
    public void launchMainScreen() {
        // TODO: 27/05/2017 implement
    }

    @Override
    public void showNetworkConnectionError() {
        // TODO: 27/05/2017 implement
    }

    @Override
    public void showUnknownError() {
        // TODO: 27/05/2017 implement
    }

    @Override
    public void showRegisterUserCollisionError() {
        // TODO: 27/05/2017 implement
    }

    @Override
    public void showRegisterCredentialsDiscardedError() {
        // TODO: 27/05/2017 implement
    }

    @Override
    public void showLoginInvalidCredentialsError() {
        // TODO: 27/05/2017 implement
    }
}
