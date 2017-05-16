package pl.rmakowiecki.eventhub.ui.screen_event_calendar;

import android.location.Location;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import pl.rmakowiecki.eventhub.RxLocationProvider;
import pl.rmakowiecki.eventhub.model.local.Event;
import pl.rmakowiecki.eventhub.model.local.LocationCoordinates;
import pl.rmakowiecki.eventhub.repository.Repository;
import pl.rmakowiecki.eventhub.ui.BasePresenter;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by m1per on 18.04.2017.
 */

public class MyEventsFragmentPresenter extends BasePresenter<MyEventsFragmentView> {

    RxLocationProvider provider = new RxLocationProvider();
    Observable<LocationCoordinates> locationObservable = provider.getLocation();
    private Repository<Event> repository;
    private ArrayList<String> distances = new ArrayList<>();
    private int position;

    MyEventsFragmentPresenter(int position) {
        repository = new EventsRepository();
        this.position = position;
    }

    private void onViewInitialization() {
        acquireEvents();
    }

    private void acquireEvents() {
        repository
                .query(new MyEventsSpecifications(position) {
                })
                .compose(applySchedulers())
                .subscribe(events -> {
                    passCompleteData(events);
                });
    }

    private void passCompleteData(List<Event> events) {
        provider.getLocation()
                .filter(location -> location != null)
                .compose(applySchedulers())
                .subscribe(
                        coordinates -> {
                            calculateDistances(coordinates, events);
                            view.showEvents(events, distances);
                        });
    }

    private void calculateDistances(LocationCoordinates coordinates, List<Event> events) {
        Location userLocation = new Location("");
        String distance;
        double distanceInMeters;
        double calculatedDistance;
        String eventCoords[];

        userLocation.setLatitude(coordinates.getLatitude());
        userLocation.setLongitude(coordinates.getLongitude());
        for (Event event : events) {
            eventCoords = event.getCoordinates().split(",");
            Location eventLocation = new Location("");
            eventLocation.setLatitude(Double.parseDouble(eventCoords[0]));
            eventLocation.setLongitude(Double.parseDouble(eventCoords[1]));

            distanceInMeters = (userLocation.distanceTo(eventLocation));
            if (distanceInMeters < 1000) {
                distance = Integer.toString((int) distanceInMeters);
                distances.add(distance + " m");
            } else {
                calculatedDistance = BigDecimal.valueOf(distanceInMeters / 1000)
                        .setScale(1, RoundingMode.HALF_UP)
                        .doubleValue();
                distance = Double.toString(calculatedDistance);
                distances.add(distance + " km");
            }
        }
    }

    @Override
    protected void onViewStarted(MyEventsFragmentView view) {
        super.onViewStarted(view);
        onViewInitialization();
    }

    @Override
    public MyEventsFragmentView getNoOpView() {
        return NoOpMyEventsFragmentView.INSTANCE;
    }
}
