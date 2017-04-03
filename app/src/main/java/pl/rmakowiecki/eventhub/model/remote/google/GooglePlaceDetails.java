
package pl.rmakowiecki.eventhub.model.remote.google;

import java.util.List;

public class GooglePlaceDetails {
    public final List<AddressComponent> addressComponents;
    public final String adrAddress;
    public final String formattedAddress;
    public final String formattedPhoneNumber;
    public final Geometry geometry;
    public final String icon;
    public final String id;
    public final String internationalPhoneNumber;
    public final String name;
    public final OpeningHours openingHours;
    public final List<PhotoReference> photos;
    public final String placeId;
    public final Double rating;
    public final String reference;
    public final List<Review> reviews;
    public final String scope;
    public final List<String> types;
    public final String url;
    public final Integer utcOffset;
    public final String vicinity;
    public final String website;

    public GooglePlaceDetails(List<AddressComponent> addressComponents, String adrAddress, String formattedAddress, String formattedPhoneNumber,
            Geometry geometry, String icon,
            String id,
            String internationalPhoneNumber, String name, OpeningHours openingHours, List<PhotoReference> photos, String placeId, Double rating,
            String reference,
            List<Review> reviews, String scope, List<String> types, String url,
            Integer utcOffset, String vicinity, String website) {
        this.addressComponents = addressComponents;
        this.adrAddress = adrAddress;
        this.formattedAddress = formattedAddress;
        this.formattedPhoneNumber = formattedPhoneNumber;
        this.geometry = geometry;
        this.icon = icon;
        this.id = id;
        this.internationalPhoneNumber = internationalPhoneNumber;
        this.name = name;
        this.openingHours = openingHours;
        this.photos = photos;
        this.placeId = placeId;
        this.rating = rating;
        this.reference = reference;
        this.reviews = reviews;
        this.scope = scope;
        this.types = types;
        this.url = url;
        this.utcOffset = utcOffset;
        this.vicinity = vicinity;
        this.website = website;
    }
}
