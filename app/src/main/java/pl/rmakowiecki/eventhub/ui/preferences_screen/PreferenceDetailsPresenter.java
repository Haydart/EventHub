package pl.rmakowiecki.eventhub.ui.preferences_screen;

import pl.rmakowiecki.eventhub.ui.BasePresenter;

public class PreferenceDetailsPresenter extends BasePresenter<PreferenceDetailsView> {

    private void onViewInitialization() {
        view.getPreferenceCategoryFromParcel();
        view.displayToolbarImage();
        view.changeToolbarTitles();
        view.loadAdapter();
        view.enableHomeButton();
    }

    @Override
    protected void onViewStarted(PreferenceDetailsView view) {
        super.onViewStarted(view);
        onViewInitialization();
    }

    @Override
    public PreferenceDetailsView getNoOpView() {
        return NoOpPreferenceDetailsView.INSTANCE;
    }
}
