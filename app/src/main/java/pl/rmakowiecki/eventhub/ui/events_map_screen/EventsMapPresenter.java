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

    private static final long CAMERA_MOVE_TO_LOCATION_DELAY = 3000;

    private Repository<Event> repository;
    private LocationProvider locationProvider;
    private LocationCoordinates lastKnownDeviceLocation;
    private Subscription mapCameraTransitionSubscription;

    EventsMapPresenter() {
        repository = new EventsRepository();
        locationProvider = new RxLocationProvider();
    }

    @Override
    protected void onViewStarted(EventsMapView view) {
        super.onViewStarted(view);
        appointMapTransitionTask();
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
        locationProvider
                .getLocationUpdates()
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

    void onMapCameraIdle() {
        appointMapTransitionTask();
    }

    void onMapCameraMovementStarted() {
        dismissMapTransitionTask();
    }

    private void appointMapTransitionTask() {
        mapCameraTransitionSubscription = Observable
                .timer(CAMERA_MOVE_TO_LOCATION_DELAY, TimeUnit.MILLISECONDS)
                .map(tick -> lastKnownDeviceLocation)
                .compose(applySchedulers())
                .subscribe(view::moveMapCamera);
    }

    private void dismissMapTransitionTask() {
        mapCameraTransitionSubscription.unsubscribe();
    }

    private void updateCurrentLocation(LocationCoordinates locationCoordinates) {
        lastKnownDeviceLocation = locationCoordinates;
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
