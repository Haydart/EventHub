package pl.rmakowiecki.eventhub.ui.screen_create_event;

import com.jenzz.noop.annotation.NoOp;
import java.util.List;
import pl.rmakowiecki.eventhub.ui.BaseView;
import pl.rmakowiecki.eventhub.ui.screen_preference_categories.PreferenceCategory;

@NoOp
interface EventCreationView extends BaseView {
    void showDatePickerView();

    void showTimePickerView();

    void showEventPlaceAddress();

    void showPickedDate(String date);

    void showPickedTime(String time);

    void showAvatarSelectDialog();

    void launchCameraAppIntent();

    void launchGalleryAppIntent();

    void showCategoriesList(List<PreferenceCategory> preference);

    void displayEventSubcategoryPicker(int position, PreferenceCategory category, PreferenceCategory preferenceCategory);

    void showFailureMessage();

    void showProfileSaveSuccess();

    void launchMapAndFinish();

    void showButtonProcessing();
}
