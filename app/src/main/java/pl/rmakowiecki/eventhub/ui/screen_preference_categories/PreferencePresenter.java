package pl.rmakowiecki.eventhub.ui.screen_preference_categories;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import pl.rmakowiecki.eventhub.ui.BasePresenter;
import pl.rmakowiecki.eventhub.util.PreferencesManager;
import rx.Observable;

public class PreferencePresenter extends BasePresenter<PreferenceView> {

    private static final int REQUIRED_PREFERENCES_COUNT = 3;
    private final int SHOW_BUTTON_SUCCESS_DELAY = 2000;
    private final int LAUNCH_MAP_ACTIVITY_DELAY = 3500;

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

    protected void onPreferenceImageClick(PreferenceCategory category) {
        view.launchPreferenceDetailsScreen(category);
    }

    @Override
    public PreferenceView getNoOpView() {
        return NoOpPreferenceView.INSTANCE;
    }

    public void onPreferenceSaveButtonClick(Context context, List<PreferenceCategory> preferences) {
        if (!isSaveButtonClicked) {
            if (hasSelectedEnough(context, preferences)) {
                isSaveButtonClicked = true;
                view.savePreferences();
            }
            else
                view.showNotEnoughPreferencesMessage();
        }
    }

    private boolean hasSelectedEnough(Context context, List<PreferenceCategory> preferences) {
        int selectedCategoriesCount = 0;
        for (PreferenceCategory category : preferences) {
            Set<String> subCategories = new PreferencesManager(context).getInterests(category.getTitle());
            if (!subCategories.isEmpty())
                ++selectedCategoriesCount;

            if (selectedCategoriesCount >= REQUIRED_PREFERENCES_COUNT)
                return true;
        }

        return false;
    }

    public void onPreferenceSave() {
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
                .subscribe(ignored -> {
                    view.launchMapAndFinish();
                });
    }
}
