package pl.rmakowiecki.eventhub.ui;

/**
 * Created by m1per on 20.04.2017.
 */

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;

public abstract class BaseFragment<P extends BasePresenter> extends Fragment implements BaseView {

    protected P presenter;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPresenter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutRes(), container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.onViewStarted(this);
    }

    @Override
    public void onStop() {
        if (presenter != null) {
            presenter.onViewStopped();
        }

        super.onStop();
    }

    @Override
    public void onDestroy() {
        presenter = null;
        super.onDestroy();
    }

    @LayoutRes
    protected abstract int getLayoutRes();

    protected abstract void initPresenter();
}
