package pl.rmakowiecki.eventhub.ui.screen_auth;

import android.support.annotation.NonNull;
import java.util.regex.Pattern;
import pl.rmakowiecki.eventhub.util.TextUtils;

class RegExpValidator {
    private final Pattern usernamePattern = Pattern.compile(
            "^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$"
    );

    private final Pattern passwordPattern = Pattern.compile(
            "^(?=.*\\d)(?=.*[a-zA-Z]).{8,32}$"
    );

    boolean isEmailValid(@NonNull String email) {
        return TextUtils.isNotEmpty(email) && usernamePattern.matcher(email).matches();
    }

    boolean isPasswordValid(@NonNull String password) {
        return TextUtils.isNotEmpty(password) && passwordPattern.matcher(password).matches();
    }
}
