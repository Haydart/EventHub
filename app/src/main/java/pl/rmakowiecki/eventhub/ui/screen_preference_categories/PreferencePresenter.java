package pl.rmakowiecki.eventhub.ui.screen_preference_categories;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import pl.rmakowiecki.eventhub.ui.BasePresenter;
import pl.rmakowiecki.eventhub.util.PreferencesManager;
import rx.Observable;

class PreferencePresenter extends BasePresenter<PreferenceView> {

    private static final int REQUIRED_PREFERENCES_COUNT = 3;
    private static final int SHOW_BUTTON_SUCCESS_DELAY = 2000;
    private static final int LAUNCH_MAP_ACTIVITY_DELAY = 3500;

    private boolean isSaveButtonClicked = false;

    private void onViewInitialization() {
        view.initPreferences();
    }

    @Override
    protected void onViewStarted(PreferenceView view) {
        super.onViewStarted(view);
        onViewInitialization();
        view.enableHomeButton();
    }

    void onPreferenceImageClick(PreferenceCategory category) {
        view.launchPreferenceDetailsScreen(category);
    }

    void onPreferenceSaveButtonClick(PreferencesManager manager, List<PreferenceCategory> preferences) {
        if (!isSaveButtonClicked) {
            if (hasSelectedEnough(manager, preferences)) {
                isSaveButtonClicked = true;
                view.savePreferences();
            }
            else
                view.showNotEnoughPreferencesMessage();
        }
    }

    private boolean hasSelectedEnough(PreferencesManager manager, List<PreferenceCategory> preferences) {
        int selectedCategoriesCount = 0;
        for (PreferenceCategory category : preferences) {
            Set<String> subCategories = manager.getInterests(category.getTitle());
            if (!subCategories.isEmpty())
                ++selectedCategoriesCount;

            if (selectedCategoriesCount >= REQUIRED_PREFERENCES_COUNT)
                return true;
        }
        return false;
    }

    void onPreferenceSave() {
        delayButtonAnimation();
        delayMapLaunch();
    }

    private void delayButtonAnimation() {
        Observable.timer(SHOW_BUTTON_SUCCESS_DELAY, TimeUnit.MILLISECONDS)
                .compose(applySchedulers())
                .subscribe(ignored -> {
                    view.showPreferencesSavingSuccess();
                });
    }

    private void delayMapLaunch() {
        Observable.timer(LAUNCH_MAP_ACTIVITY_DELAY, TimeUnit.MILLISECONDS)
                .compose(applySchedulers())
                .subscribe(ignored -> view.launchMapAndFinish());
    }

    @Override
    public PreferenceView getNoOpView() {
        return NoOpPreferenceView.INSTANCE;
    }
}
