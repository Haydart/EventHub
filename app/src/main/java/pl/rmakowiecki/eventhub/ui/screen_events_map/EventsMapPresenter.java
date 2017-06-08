package pl.rmakowiecki.eventhub.ui.screen_events_map;

import java.util.concurrent.TimeUnit;
import pl.rmakowiecki.eventhub.LocationProvider;
import pl.rmakowiecki.eventhub.RxLocationProvider;
import pl.rmakowiecki.eventhub.model.local.LocationCoordinates;
import pl.rmakowiecki.eventhub.model.local.Place;
import pl.rmakowiecki.eventhub.ui.BasePresenter;
import pl.rmakowiecki.eventhub.util.UserAuthManager;
import rx.Observable;
import rx.Subscription;
import rx.subscriptions.Subscriptions;

class EventsMapPresenter extends BasePresenter<EventsMapView> {

    private static final int DEFAULT_CAMERA_TRANSITION_DELAY = 5000;
    private static final int IMMEDIATE_CAMERA_TRANSITION_DELAY = 0;
    private static final int LOCATION_SAFETY_DELAY = 500;
    private static final int DEFAULT_MAP_PADDING = 0;
    private static final int SEARCH_BAR_MAP_TOP_PADDING = 172;
    private static final int BOTTOM_SHEET_MAP_PADDING = 300;
    private static final int MAP_TRANSITION_DELAY = 100;

    private PlacesRepository placesRepository;
    private LocationProvider locationProvider;
    private LocationCoordinates lastKnownDeviceLocation;
    private LocationCoordinates focusedMarkerLocation;
    private Subscription mapTransitionSubscription = Subscriptions.unsubscribed();
    private UserAuthManager authManager;
    
    private String clickedMarkerAddress;
    private boolean isMapClickMarkerShown = false;
    private boolean isFocusedOnProvidedMarker = false;
    private boolean isSearchBarShown = true;
    private boolean shouldMapBeInitializedToDeviceLocation;

    EventsMapPresenter() {
        placesRepository = new PlacesRepository();
        locationProvider = new RxLocationProvider();
        authManager = new UserAuthManager();
    }

    @Override
    protected void onViewStarted(EventsMapView view) {
        super.onViewStarted(view);
        if (!isMapClickMarkerShown) shouldMapBeInitializedToDeviceLocation = true;
    }

    @Override
    protected void onViewStopped() {
        super.onViewStopped();
        dismissMapTransitionTask();
    }

    void onViewInitialization() {
        view.initMap();
        promptForLocalizationSettings();
    }

    private void promptForLocalizationSettings() {
        Observable.timer(LOCATION_SAFETY_DELAY, TimeUnit.MILLISECONDS)
                .flatMap(ignored -> locationProvider.isLocationTurnedOn())
                .filter(response -> response != null)
                .compose(applySchedulers())
                .subscribe(status -> {
                    if (status.isLocationSettingRequired()) {
                        view.showLocationSettingsDialog(status);
                    }
                });
    }

    void onMapInitialized() {
        placesRepository
                .query(new LocationSpecification(lastKnownDeviceLocation))
                .compose(applySchedulers())
                .subscribe(
                        view::showPlaces,
                        Throwable::printStackTrace
                );
    }

    void onLocationPermissionsGranted() {
        locationProvider.getLocationUpdates()
                .filter(location -> location != null)
                .compose(applySchedulers())
                .subscribe(
                        this::updateCurrentLocation,
                        Throwable::printStackTrace
                );
    }

    void onMapMarkerClicked(Place place) {
        if (place != null) {
            isFocusedOnProvidedMarker = true;
            view.setBottomSheetData(place.getName(), place.getAddress());
            view.showBottomSheet();
            view.setMapPadding(
                    DEFAULT_MAP_PADDING,
                    DEFAULT_MAP_PADDING,
                    DEFAULT_MAP_PADDING,
                    BOTTOM_SHEET_MAP_PADDING);
            hideSearchBar();
            dismissMapTransitionTask();
            appointMapTransitionTask(focusedMarkerLocation = place.getLocationCoordinates(), 0);
            if (isMapClickMarkerShown) {
                isMapClickMarkerShown = false;
                view.hideMapMarker();
            }
        }
    }

    void onMapClicked(LocationCoordinates location) {
        if (!isMapClickMarkerShown) {
            if (isFocusedOnProvidedMarker) {
                isFocusedOnProvidedMarker = false;
                focusedMarkerLocation = null;
                view.hideBottomSheet();
                view.setMapPadding(
                        DEFAULT_MAP_PADDING,
                        SEARCH_BAR_MAP_TOP_PADDING,
                        DEFAULT_MAP_PADDING,
                        DEFAULT_MAP_PADDING);
                showSearchBar();
                dismissMapTransitionTask();
                appointMapTransitionTask(lastKnownDeviceLocation, DEFAULT_CAMERA_TRANSITION_DELAY);
            } else {
                isMapClickMarkerShown = true;
                view.showMapMarker(focusedMarkerLocation = location);
                view.fetchAddressForLocation(focusedMarkerLocation);
                view.showBottomSheet();
                view.setMapPadding(
                        DEFAULT_MAP_PADDING,
                        DEFAULT_MAP_PADDING,
                        DEFAULT_MAP_PADDING,
                        BOTTOM_SHEET_MAP_PADDING);
                hideSearchBar();
                dismissMapTransitionTask();
                appointMapTransitionTask(focusedMarkerLocation, 0);
            }
        } else {
            isMapClickMarkerShown = false;
            focusedMarkerLocation = null;
            view.hideMapMarker();
            view.hideBottomSheet();
            view.setMapPadding(
                    DEFAULT_MAP_PADDING,
                    SEARCH_BAR_MAP_TOP_PADDING,
                    DEFAULT_MAP_PADDING,
                    DEFAULT_MAP_PADDING);
            showSearchBar();
            dismissMapTransitionTask();
            appointMapTransitionTask(lastKnownDeviceLocation, DEFAULT_CAMERA_TRANSITION_DELAY);
        }
    }

    void onLocationAddressFetched(String addressOutput) {
        clickedMarkerAddress = addressOutput;
        view.setBottomSheetData(addressOutput, focusedMarkerLocation.toString());
        view.showBottomSheetFab();
    }

    void onPlaceSearchError() {
        // TODO: 12/04/2017 implement
    }

    void onSearchedPlaceSelected(Place place) {
        if (isMapClickMarkerShown) {
            view.hideMapMarker();
        }
        isMapClickMarkerShown = true;
        view.showMapMarker(place.getLocationCoordinates());
        view.setBottomSheetData(place.getName(), place.getAddress());
        view.showBottomSheet();
        hideSearchBar();
        dismissMapTransitionTask();
        appointMapTransitionTask(focusedMarkerLocation = place.getLocationCoordinates(), IMMEDIATE_CAMERA_TRANSITION_DELAY);
    }

    void onMapCameraIdle() {
        if (isMapClickMarkerShown || isFocusedOnProvidedMarker) {
            appointMapTransitionTask(focusedMarkerLocation, DEFAULT_CAMERA_TRANSITION_DELAY);
        } else {
            appointMapTransitionTask(lastKnownDeviceLocation, DEFAULT_CAMERA_TRANSITION_DELAY);
        }
    }

    void onMapCameraMove() {
        dismissMapTransitionTask();
    }

    private void hideSearchBar() {
        if (isSearchBarShown) {
            view.hideSearchBar(true);
            isSearchBarShown = false;
        }
    }

    private void showSearchBar() {
        if (!isSearchBarShown) {
            view.showSearchBar(true);
            isSearchBarShown = true;
        }
    }

    private void appointMapTransitionTask(LocationCoordinates location, int delay) {
        mapTransitionSubscription = Observable
                .timer(delay, TimeUnit.MILLISECONDS)
                .map(tick -> location)
                .filter(mapTargetLocation -> mapTargetLocation != null)
                .compose(applySchedulers())
                .subscribe(view::moveMapCamera);
    }

    void onCalendarMenuOptionClicked() {
        view.launchCalendarScreen();
    }

    void onPreferencesScreenMenuOptionClicked() {
        view.launchPreferencesScreen();
    }

    void onFabAnimationComplete() {
        view.launchEventCreationScreen(clickedMarkerAddress, focusedMarkerLocation);
    }

    void onSignInMenuOptionClicked() {
        view.launchSignInScreen();
    }

    void onUserProfileMenuOptionClicked() {
        view.launchUserProfileScreen();
    }

    void onLogoutMenuOptionClicked() {
        logoutUser();
    }

    private void logoutUser() {
        authManager.logoutUser();
        view.updateNavigationDrawer(false);
    }

    void onActivityResume() {
        view.updateNavigationDrawer(authManager.isUserAuthorized());
    }

    private void dismissMapTransitionTask() {
        mapTransitionSubscription.unsubscribe();
    }

    private void updateCurrentLocation(LocationCoordinates locationCoordinates) {
        if (shouldMapBeInitializedToDeviceLocation) {
            shouldMapBeInitializedToDeviceLocation = false;
            appointMapTransitionTask(lastKnownDeviceLocation = locationCoordinates, MAP_TRANSITION_DELAY);
            view.showDeviceLocation();
        }
    }

    @Override
    public EventsMapView getNoOpView() {
        return NoOpEventsMapView.INSTANCE;
    }
}
