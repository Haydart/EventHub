package pl.rmakowiecki.eventhub.ui.screen_events_map;

import java.util.ArrayList;
import java.util.List;
import pl.rmakowiecki.eventhub.api.INearbyPlacesListApiInteractor;
import pl.rmakowiecki.eventhub.api.NearbyPlacesListApiInteractor;
import pl.rmakowiecki.eventhub.model.local.Place;
import pl.rmakowiecki.eventhub.model.mappers.ModelMapper;
import pl.rmakowiecki.eventhub.model.mappers.PlaceModelMapper;
import pl.rmakowiecki.eventhub.model.remote.google.GooglePlace;
import pl.rmakowiecki.eventhub.repository.GenericQueryStatus;
import pl.rmakowiecki.eventhub.repository.QueryList;
import pl.rmakowiecki.eventhub.repository.AddOperationRepository;
import pl.rmakowiecki.eventhub.repository.Specification;
import pl.rmakowiecki.eventhub.util.GoogleApiUtil;
import rx.Observable;

class PlacesRepository implements AddOperationRepository<Place, GenericQueryStatus>, QueryList<Place> {

    private INearbyPlacesListApiInteractor nearbyPlacesInteractor;
    private ModelMapper<GooglePlace, Place> googlePlaceModelMapper;

    PlacesRepository() {
        this.nearbyPlacesInteractor = new NearbyPlacesListApiInteractor();
        this.googlePlaceModelMapper = new PlaceModelMapper();
    }

    @Override
    public Observable<GenericQueryStatus> add(Place item) {
        return Observable.empty();
    }

    @Override
    public Observable<GenericQueryStatus> add(Iterable<Place> items) {
        return Observable.empty();
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
