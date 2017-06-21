package pl.rmakowiecki.eventhub.ui.screen_auth;

import pl.rmakowiecki.eventhub.model.remote.credentials.AuthCredentials;
import pl.rmakowiecki.eventhub.util.TextUtils;

public class CredentialsValidator {
    private final CredentialsValidationCallback callback;
    private final RegExpValidator regExpMatcher;

    public CredentialsValidator(CredentialsValidationCallback callback) {
        this.callback = callback;
        this.regExpMatcher = new RegExpValidator();
    }

    public void validateCredentials(AuthCredentials authCredentials, AuthPerspective authPerspective) {
        if (authPerspective == AuthPerspective.LOGIN) {
            boolean isEmailValid = checkEmail(authCredentials.getEmail());
            boolean isPasswordValid = checkPassword(authCredentials.getPassword());

            if (areAllCredentialsValid(isEmailValid, isPasswordValid) && areFieldsFilled(authCredentials.getEmail(), authCredentials.getPassword())) {
                callback.onAllCredentialsValid();
            }
        } else {
            String email = authCredentials.getEmail();
            String password = authCredentials.getPassword();
            String passwordRepeat = authCredentials.getRepeatedPassword();

            boolean isEmailValid = checkEmail(email);
            boolean isPasswordValid = checkPassword(password);
            boolean isPasswordRepeatValid = checkPasswordRepeat(password, passwordRepeat);
            boolean arePasswordsFilled = areFieldsFilled(email, password, passwordRepeat);

            if (areAllCredentialsValid(isEmailValid, isPasswordValid, isPasswordRepeatValid, arePasswordsFilled)) {
                callback.onAllCredentialsValid();
            }
        }
    }

    private boolean areFieldsFilled(String email, String password) {
        return TextUtils.isNotEmpty(email) && TextUtils.isNotEmpty(password);
    }

    private boolean areFieldsFilled(String email, String password, String passwordRepeat) {
        return TextUtils.isNotEmpty(email) && TextUtils.isNotEmpty(password) && TextUtils.isNotEmpty(passwordRepeat);
    }

    private boolean areAllCredentialsValid(boolean isEmailValid, boolean isPasswordValid) {
        return isEmailValid && isPasswordValid;
    }

    private boolean areAllCredentialsValid(boolean isEmailValid, boolean isPasswordValid, boolean isPasswordRepeatValid, boolean arePasswordsFilled) {
        return isEmailValid && isPasswordValid && isPasswordRepeatValid && arePasswordsFilled;
    }

    private boolean checkEmail(String email) {
        if (TextUtils.isEmpty(email) || regExpMatcher.isEmailValid(email)) {
            return true;
        } else {
            callback.onEmailInvalid();
            return false;
        }
    }

    private boolean checkPassword(String password) {
        if (TextUtils.isEmpty(password) || regExpMatcher.isPasswordValid(password)) {
            return true;
        } else {
            callback.onPasswordInvalid();
            return false;
        }
    }

    private boolean checkPasswordRepeat(String password, String passwordRepeat) {
        if (TextUtils.isEmpty(passwordRepeat) || password.equals(passwordRepeat)) {
            return true;
        } else {
            callback.onPasswordRepeatInvalid();
            return false;
        }
    }

    interface CredentialsValidationCallback {
        void onAllCredentialsValid();

        void onEmailInvalid();

        void onPasswordInvalid();

        void onPasswordRepeatInvalid();
    }
}
