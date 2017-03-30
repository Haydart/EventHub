
package pl.rmakowiecki.eventhub.model.remote.google;

import java.util.List;

public class Review {
    public final List<Aspect> aspects;
    public final String authorName;
    public final String authorUrl;
    public final String language;
    public final String profilePhotoUrl;
    public final Integer rating;
    public final String relativeTimeDescription;
    public final String text;
    public final Integer time;

    public Review(List<Aspect> aspects, String authorName, String authorUrl, String language, String profilePhotoUrl, Integer rating, String relativeTimeDescription, String text,
            Integer time) {
        this.aspects = aspects;
        this.authorName = authorName;
        this.authorUrl = authorUrl;
        this.language = language;
        this.profilePhotoUrl = profilePhotoUrl;
        this.rating = rating;
        this.relativeTimeDescription = relativeTimeDescription;
        this.text = text;
        this.time = time;
    }
}
