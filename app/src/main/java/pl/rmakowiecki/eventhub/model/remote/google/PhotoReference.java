
package pl.rmakowiecki.eventhub.model.remote.google;

import java.util.List;

public class PhotoReference {
    public final long height;
    public final List<String> htmlAttributions;
    public final String photoReference;
    public final long width;

    public PhotoReference(long height, List<String> htmlAttributions, String photoReference, long width) {
        this.height = height;
        this.htmlAttributions = htmlAttributions;
        this.photoReference = photoReference;
        this.width = width;
    }
}
