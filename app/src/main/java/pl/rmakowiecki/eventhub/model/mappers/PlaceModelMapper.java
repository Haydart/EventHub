package pl.rmakowiecki.eventhub.model.mappers;

import pl.rmakowiecki.eventhub.model.local.LocationCoordinates;
import pl.rmakowiecki.eventhub.model.local.Place;
import pl.rmakowiecki.eventhub.model.remote.google.Coordinates;
import pl.rmakowiecki.eventhub.model.remote.google.GooglePlace;

public class PlaceModelMapper implements ModelMapper<GooglePlace, Place> {
    @Override
    public Place map(GooglePlace googlePlace) {
        Coordinates googlePlaceLocation = googlePlace.geometry.location;
        LocationCoordinates placeLocation = new LocationCoordinates(googlePlaceLocation.latitude, googlePlaceLocation.longitude);
        return new Place(googlePlace.placeId, googlePlace.name, googlePlace.vicinity, placeLocation);
    }
}
