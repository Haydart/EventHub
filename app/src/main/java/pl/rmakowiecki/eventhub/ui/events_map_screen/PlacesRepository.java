package pl.rmakowiecki.eventhub.ui.events_map_screen;

import java.util.ArrayList;
import java.util.List;
import pl.rmakowiecki.eventhub.api.INearbyPlacesListApiInteractor;
import pl.rmakowiecki.eventhub.api.NearbyPlacesListApiInteractor;
import pl.rmakowiecki.eventhub.model.local.Place;
import pl.rmakowiecki.eventhub.model.remote.google.GooglePlace;
import pl.rmakowiecki.eventhub.repository.ModelMapper;
import pl.rmakowiecki.eventhub.repository.Repository;
import pl.rmakowiecki.eventhub.repository.Specification;
import pl.rmakowiecki.eventhub.util.GoogleApiUtil;
import rx.Observable;

public class PlacesRepository implements Repository<Place> {

    private INearbyPlacesListApiInteractor nearbyPlacesInteractor;
    private ModelMapper<GooglePlace, Place> googlePlaceModelMapper;

    public PlacesRepository() {
        this.nearbyPlacesInteractor = new NearbyPlacesListApiInteractor();
        this.googlePlaceModelMapper = new PlaceModelMapper();
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
                .flatMap(this::convertToLocalModel);
    }

    private Observable<List<Place>> convertToLocalModel(List<GooglePlace> googlePlaces) {
        List<Place> placesList = new ArrayList<>(googlePlaces.size());
        for (GooglePlace googlePlace : googlePlaces) {
            placesList.add(googlePlaceModelMapper.map(googlePlace));
        }
        return Observable.just(placesList);
    }
}
