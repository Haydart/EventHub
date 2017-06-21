package pl.rmakowiecki.eventhub.ui.screen_auth;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;
import pl.rmakowiecki.eventhub.R;
import pl.rmakowiecki.eventhub.model.local.User;
import pl.rmakowiecki.eventhub.ui.BaseActivity;
import pl.rmakowiecki.eventhub.ui.custom_view.ActionButton;
import pl.rmakowiecki.eventhub.ui.screen_personalization.PersonalizationActivity;
import pl.rmakowiecki.eventhub.ui.screen_user_profile.UserProfileRepository;
import pl.rmakowiecki.eventhub.util.BitmapUtils;
import pl.rmakowiecki.eventhub.util.PreferencesManager;

public class AuthActivity extends BaseActivity<AuthPresenter> implements AuthView {

    private static final int RC_SIGN_IN = 1;

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
    private GoogleApiClient googleApiClient;
    private PreferencesManager preferencesManager;
    private UserProfileRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferencesManager = new PreferencesManager(this);
        repository = new UserProfileRepository();
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerCredentialsChanges();
        setupFacebookLoginCallbacks();
        setupGoogleLoginClient();
    }

    private void setupGoogleLoginClient() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, connectionResult -> presenter.onGoogleLoginError())
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
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

        if (requestCode == RC_SIGN_IN) {
            presenter.onGoogleLoginResult(Auth.GoogleSignInApi.getSignInResultFromIntent(data));
        }
        else {
            facebookCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void initPresenter() {
        presenter = new AuthPresenter(new PreferencesManager(this));
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

    @OnClick(R.id.image_view_login_g)
    public void onGoogleLoginButtonClicked() {
        presenter.onGoogleLoginButtonClicked();
    }

    @Override
    public void loginWithFacebookAuthentication(List<String> readPermissionsList) {
        LoginManager.getInstance().logInWithReadPermissions(this, readPermissionsList);
    }

    @Override
    public void loginWithGoogleAuthentication() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void showFacebookLoginSuccess() {
        // TODO: 17/06/2017 rework
        Toast.makeText(this, "Facebook login success", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showFacebookLoginError() {
        // TODO: 17/06/2017 rework
        Toast.makeText(this, "Could not login with Facebook", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showGoogleLoginSuccess() {
        // TODO: 17/06/2017 rework
        Toast.makeText(this, "Google login success", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void saveUserGoogleData(GoogleSignInAccount signInAccount) {
        Picasso.with(this)
                .load(signInAccount.getPhotoUrl())
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        createUserFromGoogleData(signInAccount, bitmap);
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                        //no-op
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                        //no-op
                    }
                });
    }

    private void createUserFromGoogleData(GoogleSignInAccount signInAccount, Bitmap bitmap) {
        User user = new User(signInAccount.getDisplayName(), BitmapUtils.getBytesFromBitmap(bitmap));
        preferencesManager.saveUserDataLocally(user);
        repository.add(user);
    }

    @Override
    public void showGoogleLoginError() {
        // TODO: 17/06/2017 rework
        Toast.makeText(this, "Could not login with Google", Toast.LENGTH_SHORT).show();
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
    public void launchMainScreen() {
        finish();
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
