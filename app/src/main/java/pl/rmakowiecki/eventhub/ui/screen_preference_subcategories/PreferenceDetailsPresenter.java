package pl.rmakowiecki.eventhub.ui.screen_preference_subcategories;

import pl.rmakowiecki.eventhub.ui.BasePresenter;

public class PreferenceDetailsPresenter extends BasePresenter<PreferenceDetailsView> {

    private void onViewInitialization() {
        view.displayToolbarImage();
        view.changeToolbarTitles();
        view.loadAdapter();
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
