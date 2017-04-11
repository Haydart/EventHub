package pl.rmakowiecki.eventhub.ui.start_screen;

import android.content.Intent;
import android.os.Bundle;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.OnClick;
import pl.rmakowiecki.eventhub.R;
import pl.rmakowiecki.eventhub.ui.BaseActivity;
import pl.rmakowiecki.eventhub.ui.events_map_screen.EventsMapActivity;
import pl.rmakowiecki.eventhub.ui.preferences_screen.PreferenceActivity;

public class AuthActivity extends BaseActivity<AuthPresenter> implements AuthView {

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void initPresenter() {
        presenter = new AuthPresenter();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.auth_layout;
    }

    @OnClick(R.id.auth_event_button)
    protected void onEventButtonClick() {
        presenter.onEventButtonClick();
    }

    @OnClick(R.id.auth_preferences_button)
    protected void onPreferencesButtonClick() {
        presenter.onPreferencesButtonClick();
    }

    @Override
    public void launchEvents() {
        String email = "example@gmail.com";
        String password = "firebasepassword";
        firebaseAuth
                .signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(this, EventsMapActivity.class);
                        startActivity(intent);
                    }
                });
    }

    @Override
    public void launchPreferences() {
        String email = "example@gmail.com";
        String password = "firebasepassword";
        firebaseAuth
                .signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(this, PreferenceActivity.class);
                        startActivity(intent);
                    }
                });
    }
}
