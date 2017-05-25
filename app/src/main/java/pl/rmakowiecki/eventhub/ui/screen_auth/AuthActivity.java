package pl.rmakowiecki.eventhub.ui.screen_auth;

import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.FrameLayout;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import com.jakewharton.rxbinding.widget.RxTextView;
import pl.rmakowiecki.eventhub.R;
import pl.rmakowiecki.eventhub.ui.BaseActivity;
import pl.rmakowiecki.eventhub.ui.custom_view.ActionButton;

public class AuthActivity extends BaseActivity<AuthPresenter> implements AuthView {

    @BindView(R.id.input_layout_email) TextInputLayout emailInputLayout;
    @BindView(R.id.input_layout_password) TextInputLayout passwordInputLayout;
    @BindView(R.id.input_layout_repeat_password) TextInputLayout passwordRepeatInputLayout;
    @BindView(R.id.edit_text_email) TextInputEditText emailEditText;
    @BindView(R.id.edit_text_password) TextInputEditText passwordEditText;
    @BindView(R.id.edit_text_repeat_password) TextInputEditText passwordRepeatEditText;
    @BindView(R.id.login_bottom_layout) FrameLayout loginOptionsBottomLayout;
    @BindView(R.id.auth_action_button) ActionButton authActionButton;

    @BindString(R.string.button_text_sign_in) String signInButtonText;
    @BindString(R.string.button_text_sign_up) String signUpButtonText;

    @Override
    protected void onStart() {
        super.onStart();
        registerCredentialsChanges();
    }

    private void registerCredentialsChanges() {
        presenter.onInputCredentialsChanged(
                RxTextView.textChanges(emailEditText),
                RxTextView.textChanges(passwordEditText),
                RxTextView.textChanges(passwordRepeatEditText)
        );
    }

    @OnClick(R.id.registration_form_button)
    public void onButtonClicked() {
        presenter.onRegistrationFormButtonClicked();
    }

    @OnClick(R.id.auth_action_button)
    public void onAuthActionButtonClicked() {
        presenter.onAuthActionButtonClicked();
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
    public void onBackPressed() {
        presenter.onBackButtonPressed();
    }

    @Override
    protected void initPresenter() {
        presenter = new AuthPresenter();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_auth;
    }
}
