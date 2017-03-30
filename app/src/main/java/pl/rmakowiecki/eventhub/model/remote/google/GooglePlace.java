
package pl.rmakowiecki.eventhub.model.remote.google;

import java.util.List;

public class GooglePlace {
    public final Geometry geometry;
    public final String icon;
    public final String id;
    public final String name;
    public final OpeningHours openingHours;
    public final List<PhotoReference> photos;
    public final String placeId;
    public final double rating;
    public final String reference;
    public final String scope;
    public final List<String> types;
    public final String vicinity;

    public GooglePlace(String vicinity, String scope, String reference, double rating, String placeId, OpeningHours openingHours,
            final String name, String id, String icon, Geometry geometry, List<PhotoReference> photos, List<String> types) {

        this.vicinity = vicinity;
        this.scope = scope;
        this.reference = reference;
        this.rating = rating;
        this.placeId = placeId;
        this.openingHours = openingHours;
        this.name = name;
        this.id = id;
        this.icon = icon;
        this.geometry = geometry;
        this.photos = photos;
        this.types = types;
    }
}
