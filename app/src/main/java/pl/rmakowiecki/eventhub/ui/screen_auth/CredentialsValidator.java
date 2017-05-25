package pl.rmakowiecki.eventhub.ui.screen_auth;

import pl.rmakowiecki.eventhub.model.remote.credentials.AuthCredentials;
import pl.rmakowiecki.eventhub.util.TextUtils;

class CredentialsValidator {
    private final CredentialsValidationCallback callback;
    private final RegExpValidator regExpMatcher;

    CredentialsValidator(CredentialsValidationCallback callback) {
        this.callback = callback;
        this.regExpMatcher = new RegExpValidator();
    }

    void validateCredentials(AuthCredentials authCredentials, AuthPerspective authPerspective) {
        if (authPerspective == AuthPerspective.LOGIN) {
            boolean isEmailValid = checkEmail(authCredentials.getEmail());
            boolean isPasswordValid = checkPassword(authCredentials.getPassword());

            if (areAllCredentialsValid(isEmailValid, isPasswordValid)) {
                callback.onAllCredentialsValid();
            }
        } else {
            String email = authCredentials.getEmail();
            String password = authCredentials.getPassword();
            String passwordRepeat = authCredentials.getRepeatedPassword();

            boolean isEmailValid = checkEmail(email);
            boolean isPasswordValid = checkPassword(password);
            boolean isPasswordRepeatValid = checkPasswordRepeat(password, passwordRepeat);
            boolean arePasswordsFilled = arePasswordsFilled(password, passwordRepeat);

            if (areAllCredentialsValid(isEmailValid, isPasswordValid, isPasswordRepeatValid, arePasswordsFilled)) {
                callback.onAllCredentialsValid();
            }
        }
    }

    private boolean areAllCredentialsValid(boolean isEmailValid, boolean isPasswordValid) {
        return isEmailValid && isPasswordValid;
    }

    private boolean areAllCredentialsValid(boolean isEmailValid, boolean isPasswordValid, boolean isPasswordRepeatValid, boolean arePasswordsFilled) {
        return isEmailValid && isPasswordValid && isPasswordRepeatValid && arePasswordsFilled;
    }

    private boolean arePasswordsFilled(String password, String passwordRepeat) {
        return TextUtils.isNotEmpty(password) && TextUtils.isNotEmpty(passwordRepeat);
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
