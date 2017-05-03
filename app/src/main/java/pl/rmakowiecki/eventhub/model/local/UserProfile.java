package pl.rmakowiecki.eventhub.model.local;

import pl.rmakowiecki.eventhub.repository.DataItem;

public class UserProfile implements DataItem {
    private final byte[] pictureData;

    public UserProfile(byte[] pictureData) {
        this.pictureData = pictureData;
    }

    public byte[] getPictureData() {
        return pictureData;
    }
}
