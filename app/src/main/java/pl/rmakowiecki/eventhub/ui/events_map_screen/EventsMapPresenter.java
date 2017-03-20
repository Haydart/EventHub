package pl.rmakowiecki.eventhub.ui.events_map_screen;

import java.util.concurrent.TimeUnit;
import pl.rmakowiecki.eventhub.LocationProvider;
import pl.rmakowiecki.eventhub.RxLocationProvider;
import pl.rmakowiecki.eventhub.api.EventsSpecification;
import pl.rmakowiecki.eventhub.model.local.Event;
import pl.rmakowiecki.eventhub.model.local.LocationCoordinates;
import pl.rmakowiecki.eventhub.repository.EventsRepository;
import pl.rmakowiecki.eventhub.repository.Repository;
import pl.rmakowiecki.eventhub.ui.BasePresenter;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
class EventsMapPresenter extends BasePresenter<EventsMapView> {

    private static final int CAMERA_MOVE_TO_LOCATION_DELAY = 1000;

    private Repository<Event> repository;
    private LocationProvider locationProvider;
    private LocationCoordinates lastKnownDeviceLocation;
    private LocationCoordinates markerLocation;
    private Subscription mapTransitionSubscription;

    private boolean isMapClickMarkerShown = false;
    private boolean shouldMapBeInitializedToDeviceLocation;

    EventsMapPresenter() {
        repository = new EventsRepository();
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
                        this::updateCurrentLocation,
                        Throwable::printStackTrace
                );
    }

    void onMapViewInitialized() {
        repository
                .query(new EventsSpecification() {
                })
                .filter(events -> events != null && !events.isEmpty())
                .compose(applySchedulers())
                .subscribe(view::showEvents);
    }

    void onMapClicked(LocationCoordinates locationCoordinates) {
        isMapClickMarkerShown = !isMapClickMarkerShown;
        if (isMapClickMarkerShown) {
            markerLocation = locationCoordinates;
            view.showMapClickMarker(locationCoordinates);
            view.showBottomSheet();
            view.setBottomMapPadding();
            dismissMapTransitionTask();
            appointMapTransitionTask(markerLocation, 0);
        } else {
            markerLocation = null;
            view.hideMapClickMarker(locationCoordinates);
            view.hideBottomSheet();
            view.setDefaultMapPadding();
            dismissMapTransitionTask();
            appointMapTransitionTask(lastKnownDeviceLocation, CAMERA_MOVE_TO_LOCATION_DELAY);
        }
    }

    void onMapCameraIdle() {
        if (isMapClickMarkerShown) {
            appointMapTransitionTask(markerLocation, CAMERA_MOVE_TO_LOCATION_DELAY);
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
