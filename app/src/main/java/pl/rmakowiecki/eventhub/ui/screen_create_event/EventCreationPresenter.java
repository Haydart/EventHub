package pl.rmakowiecki.eventhub.ui.screen_create_event;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import pl.rmakowiecki.eventhub.model.local.Event;
import pl.rmakowiecki.eventhub.model.local.EventAttendee;
import pl.rmakowiecki.eventhub.model.local.LocationCoordinates;
import pl.rmakowiecki.eventhub.repository.QueryStatus;
import pl.rmakowiecki.eventhub.repository.Repository;
import pl.rmakowiecki.eventhub.ui.AvatarPickDialogFragment;
import pl.rmakowiecki.eventhub.ui.BasePresenter;
import pl.rmakowiecki.eventhub.ui.screen_event_calendar.EventsRepository;
import pl.rmakowiecki.eventhub.ui.screen_preference_categories.PreferenceCategory;
import pl.rmakowiecki.eventhub.ui.screen_preference_categories.PreferencesRepository;
import pl.rmakowiecki.eventhub.ui.screen_preference_categories.PreferencesSpecification;
import pl.rmakowiecki.eventhub.util.UserAuthManager;

class EventCreationPresenter extends BasePresenter<EventCreationView> {

    private Calendar eventTime = Calendar.getInstance();
    private Repository<Event, QueryStatus> eventRepository = new EventsRepository();
    private UserAuthManager userAuthManager = new UserAuthManager();
    private List<PreferenceCategory> fullCategoriesList = new ArrayList<>();
    private List<PreferenceCategory> pickedCategoriesList = new ArrayList<>();

    @Override
    protected void onViewStarted(EventCreationView view) {
        super.onViewStarted(view);
        view.showEventPlaceAddress();
    }

    @Override
    protected void onViewVisible() {
        super.onViewVisible();

        new PreferencesRepository()
                .query(new PreferencesSpecification() {
                })
                .compose(applySchedulers())
                .subscribe((categoriesList) -> {
                    fullCategoriesList = categoriesList;
                    view.showCategoriesList(categoriesList);
                });
    }

    void onDatePickerButtonClicked() {
        view.showDatePickerView();
    }

    void onTimePickerButtonClicked() {
        view.showTimePickerView();
    }

    void onEventDatePicked(int year, int monthOfYear, int dayOfMonth) {
        eventTime.set(Calendar.YEAR, year);
        eventTime.set(Calendar.MONTH, monthOfYear);
        eventTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        view.showPickedDate(String.format(Locale.getDefault(), "%s-%s-%s", dayOfMonth, monthOfYear, year));
    }

    void onEventTimePicked(int hourOfDay, int minute) {
        eventTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        eventTime.set(Calendar.MINUTE, minute);
        view.showPickedTime(String.format(Locale.getDefault(), "%d:%d", hourOfDay, minute));
    }

    void onEventAvatarButtonClicked() {
        view.showAvatarSelectDialog();
    }

    void onPhotoOptionSelected(AvatarPickDialogFragment.AvatarPickDialogListener.AvatarSource photoSource) {
        if (photoSource == AvatarPickDialogFragment.AvatarPickDialogListener.AvatarSource.CAMERA) {
            view.launchCameraAppIntent();
        } else if (photoSource == AvatarPickDialogFragment.AvatarPickDialogListener.AvatarSource.GALLERY) {
            view.launchGalleryAppIntent();
        }
    }

    void onEventCategoryClicked(int position, PreferenceCategory fullCategory) {
        PreferenceCategory previouslyPickedCategory = null;
        if (pickedCategoriesList.contains(fullCategory)) {
            previouslyPickedCategory = pickedCategoriesList.get(pickedCategoriesList.indexOf(fullCategory));
        }
        view.displayEventSubcategoryPicker(position, fullCategory, previouslyPickedCategory);
    }

    void onSubcategoriesPicked(PreferenceCategory category, boolean[] checkedItems) {
        List<String> chosenSubcategories = new ArrayList<>();
        int indexOfCategory = fullCategoriesList.indexOf(category);
        List<String> fullCategoryChildrenList = fullCategoriesList.get(indexOfCategory).getChildList();

        for (int i = 0; i < checkedItems.length; i++) {
            if (checkedItems[i]) {
                chosenSubcategories.add(fullCategoryChildrenList.get(i));
            }
        }
        PreferenceCategory pickedCategory = new PreferenceCategory(category.getTitle(), category.getImageResourceName(), chosenSubcategories);
        if (pickedCategoriesList.contains(pickedCategory)) {
            pickedCategoriesList.remove(pickedCategory);
        }
        pickedCategoriesList.add(pickedCategory);
    }

    void onEventCreationButtonClicked(LocationCoordinates eventCoordinates, String eventName, String eventDescription, String eventAddress, String organizerName) {
        List<EventAttendee> attendees = new ArrayList<>();
        attendees.add(new EventAttendee(userAuthManager.getCurrentUserId(), organizerName));
        Event event = new Event(
                "",
                eventName,
                eventDescription,
                eventTime.getTimeInMillis(),
                organizerName,
                eventAddress,
                eventCoordinates.toString(),
                pickedCategoriesList,
                attendees
        );
        eventRepository.add(event);
    }

    @Override
    public EventCreationView getNoOpView() {
        return NoOpEventCreationView.INSTANCE;
    }
}
