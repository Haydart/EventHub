package pl.rmakowiecki.eventhub.ui.events_map_screen;

import java.util.concurrent.TimeUnit;
import pl.rmakowiecki.eventhub.LocationProvider;
import pl.rmakowiecki.eventhub.RxLocationProvider;
import pl.rmakowiecki.eventhub.model.local.LocationCoordinates;
import pl.rmakowiecki.eventhub.model.local.Place;
import pl.rmakowiecki.eventhub.repository.LocationSpecification;
import pl.rmakowiecki.eventhub.repository.PlacesRepository;
import pl.rmakowiecki.eventhub.repository.Repository;
import pl.rmakowiecki.eventhub.ui.BasePresenter;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
class EventsMapPresenter extends BasePresenter<EventsMapView> {

    private static final int CAMERA_MOVE_TO_LOCATION_DELAY = 1000;

    private Repository<Place> placesRepository;
    private LocationProvider locationProvider;
    private LocationCoordinates lastKnownDeviceLocation;
    private LocationCoordinates focusedMarkerLocation;
    private Subscription mapTransitionSubscription;

    private boolean isMapClickMarkerShown = false;
    private boolean isFocusedOnProvidedMarker = false;
    private boolean shouldMapBeInitializedToDeviceLocation;

    EventsMapPresenter() {
        placesRepository = new PlacesRepository();
        locationProvider = new RxLocationProvider();
    }

    @Override
    protected void onViewStarted(EventsMapView view) {
        super.onViewStarted(view);
        if(!isMapClickMarkerShown) shouldMapBeInitializedToDeviceLocation = true;
    }

    @Override
    protected void onViewStopped() {
        super.onViewStopped();
        dismissMapTransitionTask();
    }

    void onViewInitialization() {
        view.initMap();
    }

    void onLocationPermissionsGranted() {
        locationProvider.getLocationUpdates()
                .filter(location -> location != null)
                .compose(applySchedulers())
                .subscribe(
                        locationCoordinates -> {
                            updateCurrentLocation(locationCoordinates);
                            placesRepository
                                    .query(new LocationSpecification(lastKnownDeviceLocation))
                                    .compose(applySchedulers())
                                    .subscribe(view::showPlaces);
                        },
                        Throwable::printStackTrace
                );
    }

    void onMapViewInitialized() {
        //no-op
    }

    void onMapMarkerClicked(LocationCoordinates location) {
        isFocusedOnProvidedMarker = true;
        focusedMarkerLocation = location;
        view.showBottomSheet();
        view.setBottomMapPadding();
        dismissMapTransitionTask();
        appointMapTransitionTask(focusedMarkerLocation, 0);
        if (isMapClickMarkerShown) {
            isMapClickMarkerShown = false;
            view.hideMapClickMarker();
        }
    }

    void onMapClicked(LocationCoordinates location) {
        if (!isMapClickMarkerShown) {
            if (isFocusedOnProvidedMarker) {
                isFocusedOnProvidedMarker = false;
                focusedMarkerLocation = null;
                view.hideBottomSheet();
                view.setDefaultMapPadding();
                dismissMapTransitionTask();
                appointMapTransitionTask(lastKnownDeviceLocation, CAMERA_MOVE_TO_LOCATION_DELAY);
            } else {
                isMapClickMarkerShown = true;
                focusedMarkerLocation = location;
                view.showMapClickMarker(location);
                view.showBottomSheet();
                view.setBottomMapPadding();
                dismissMapTransitionTask();
                appointMapTransitionTask(focusedMarkerLocation, 0);
            }
        } else {
            isMapClickMarkerShown = false;
            focusedMarkerLocation = null;
            view.hideMapClickMarker();
            view.hideBottomSheet();
            view.setDefaultMapPadding();
            dismissMapTransitionTask();
            appointMapTransitionTask(lastKnownDeviceLocation, CAMERA_MOVE_TO_LOCATION_DELAY);
        }
    }

    void onMapCameraIdle() {
        if (isMapClickMarkerShown || isFocusedOnProvidedMarker) {
            appointMapTransitionTask(focusedMarkerLocation, CAMERA_MOVE_TO_LOCATION_DELAY);
        } else {
            appointMapTransitionTask(lastKnownDeviceLocation, CAMERA_MOVE_TO_LOCATION_DELAY);
        }
    }

    void onMapCameraMove() {
        dismissMapTransitionTask();
    }

    private void appointMapTransitionTask(LocationCoordinates location, int delay) {
        mapTransitionSubscription = Observable
                .timer(delay, TimeUnit.MILLISECONDS)
                .map(tick -> location)
                .filter(mapTargetLocation -> mapTargetLocation != null)
                .compose(applySchedulers())
                .subscribe(view::moveMapCamera);
    }

    private void dismissMapTransitionTask() {
        mapTransitionSubscription.unsubscribe();
    }

    private void updateCurrentLocation(LocationCoordinates locationCoordinates) {
        lastKnownDeviceLocation = locationCoordinates;
        if (shouldMapBeInitializedToDeviceLocation) {
            shouldMapBeInitializedToDeviceLocation = false;
            appointMapTransitionTask(lastKnownDeviceLocation, 100);
            view.showDeviceLocation();
        }
    }

    private <T> Observable.Transformer<T, T> applySchedulers() {
        return observable -> observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public EventsMapView getNoOpView() {
        return NoOpEventsMapView.INSTANCE;
    }
}
