package pl.rmakowiecki.eventhub.repository;

import java.util.ArrayList;
import java.util.List;
import pl.rmakowiecki.eventhub.api.INearbyPlacesListApiInteractor;
import pl.rmakowiecki.eventhub.api.NearbyPlacesListApiInteractor;
import pl.rmakowiecki.eventhub.model.local.LocationCoordinates;
import pl.rmakowiecki.eventhub.model.local.Place;
import pl.rmakowiecki.eventhub.model.remote.google.Coordinates;
import pl.rmakowiecki.eventhub.model.remote.google.GooglePlace;
import pl.rmakowiecki.eventhub.util.GoogleApiUtil;
import rx.Observable;

public class PlacesRepository implements Repository<Place> {

    INearbyPlacesListApiInteractor nearbyPlacesInteractor;

    public PlacesRepository() {
        this.nearbyPlacesInteractor = new NearbyPlacesListApiInteractor();
    }

    @Override
    public void add(Place item) {
        // TODO: 14/03/2017 implement
    }

    @Override
    public void add(Iterable<Place> items) {
        // TODO: 14/03/2017 implement
    }

    @Override
    public void update(Place item) {
        // TODO: 14/03/2017 implement
    }

    @Override
    public void remove(Place item) {
        // TODO: 14/03/2017 implement
    }

    @Override
    public Observable<List<Place>> query(Specification specification) {
        String locationString = GoogleApiUtil.constructLocationString(
                ((LocationSpecification) specification).getLocation()
        );

        return nearbyPlacesInteractor
                .loadGooglePlaces(locationString)
                .flatMap(this::convertTolocalModel);
    }

    private Observable<List<Place>> convertTolocalModel(List<GooglePlace> googlePlaces) {
        List<Place> placesList = new ArrayList<>(googlePlaces.size());
        for (GooglePlace googlePlace : googlePlaces) {
            Coordinates googlePlaceLocation = googlePlace.geometry.location;
            LocationCoordinates placeLocation = new LocationCoordinates(googlePlaceLocation.latitude, googlePlaceLocation.longitude);
            placesList.add(
                    new Place(googlePlace.placeId, googlePlace.name, googlePlace.vicinity, placeLocation)
            );
        }
        return Observable.just(placesList);
    }
}
