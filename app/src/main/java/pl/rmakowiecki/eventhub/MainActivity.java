package pl.rmakowiecki.eventhub;

import android.os.Bundle;
import pl.rmakowiecki.eventhub.ui.BaseActivity;

public class MainActivity extends BaseActivity<MainPresenter> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initPresenter() {
        presenter = new MainPresenter();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }
}
