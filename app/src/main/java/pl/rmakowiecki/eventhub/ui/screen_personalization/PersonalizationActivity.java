package pl.rmakowiecki.eventhub.ui.screen_personalization;

import android.os.Bundle;
import pl.rmakowiecki.eventhub.R;
import pl.rmakowiecki.eventhub.ui.BaseActivity;

public class PersonalizationActivity extends BaseActivity<PersonalizationPresenter> implements PersonalizationView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initPresenter() {
        presenter = new PersonalizationPresenter();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_personalization;
    }
}
