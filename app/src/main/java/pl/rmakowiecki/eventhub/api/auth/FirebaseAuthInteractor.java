package pl.rmakowiecki.eventhub.api.auth;

import com.facebook.AccessToken;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import pl.rmakowiecki.eventhub.ui.screen_auth.AuthPerspective;

public class FirebaseAuthInteractor implements IAuthInteractor {
    private FirebaseAuth auth;
    private AuthResponseInterceptor callback;
    private AuthPerspective perspective;

    public FirebaseAuthInteractor(AuthResponseInterceptor callback) {
        auth = FirebaseAuth.getInstance();
        this.callback = callback;
    }

    @Override
    public void loginUserWithEmail(String email, String password) {
        perspective = AuthPerspective.LOGIN;
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> handleAuthResponse(task, perspective));
    }

    @Override
    public void registerUserWithEmail(String email, String password) {
        perspective = AuthPerspective.REGISTER;
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> handleAuthResponse(task, perspective));
    }

    @Override
    public void loginWithFacebook(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onFacebookLoginSuccess();
                    } else {
                        callback.onFacebookLoginError();
                    }
                });
    }

    private void handleAuthResponse(Task<AuthResult> authResultTask, AuthPerspective authPerspective) {
        if (authResultTask.isSuccessful()) {
            callback.onSuccess(authPerspective);
        } else {
            recognizeErrorAndReact(authResultTask);
        }
    }

    private void recognizeErrorAndReact(Task<AuthResult> task) {
        final Exception errorThrowable = task.getException();
        if (perspective == AuthPerspective.LOGIN) {
            if (errorThrowable instanceof FirebaseAuthInvalidCredentialsException || errorThrowable instanceof FirebaseAuthInvalidUserException) {
                callback.onInvalidCredentials();
            } else if (errorThrowable instanceof FirebaseNetworkException) {
                callback.onNetworkConnectionError();
            } else {
                callback.onUnknownError();
            }
        } else if (perspective == AuthPerspective.REGISTER) {
            if (errorThrowable instanceof FirebaseAuthUserCollisionException) {
                callback.onEmailAlreadyTaken();
            } else if (errorThrowable instanceof FirebaseNetworkException) {
                callback.onNetworkConnectionError();
            } else if (errorThrowable instanceof FirebaseAuthInvalidCredentialsException) {
                callback.onCredentialsDiscarded();
            } else {
                callback.onUnknownError();
            }
        }
    }
}
