package pl.rmakowiecki.eventhub.ui.screen_auth;

import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.FrameLayout;
import butterknife.BindView;
import butterknife.OnClick;
import pl.rmakowiecki.eventhub.R;
import pl.rmakowiecki.eventhub.ui.BaseActivity;

public class AuthActivity extends BaseActivity {
    @BindView(R.id.input_layout_repeat_password) TextInputLayout passwordRepeatInputLayout;
    @BindView(R.id.login_bottom_layout) FrameLayout loginOptionsBottomLayout;

    // TODO: 07/05/2017 refactor to mvp
    @OnClick(R.id.auth_action_button)
    public void onButtonClicked() {
        if (passwordRepeatInputLayout.getVisibility() == View.GONE) {
            passwordRepeatInputLayout.setVisibility(View.VISIBLE);
            loginOptionsBottomLayout.setVisibility(View.GONE);
        } else {
            passwordRepeatInputLayout.setVisibility(View.GONE);
            loginOptionsBottomLayout.setVisibility(View.VISIBLE);
        }
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
