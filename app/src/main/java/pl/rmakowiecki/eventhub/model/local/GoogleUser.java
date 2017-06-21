package pl.rmakowiecki.eventhub.model.local;

import android.net.Uri;

public final class GoogleUser {

    private final String displayName;
    private final Uri pictureUri;

    public GoogleUser(String displayName, Uri pictureUri) {
        this.displayName = displayName;
        this.pictureUri = pictureUri;
    }

    public Uri getPhotoUrl() {
        return pictureUri;
    }

    public String getDisplayName() {
        return displayName;
    }
}
