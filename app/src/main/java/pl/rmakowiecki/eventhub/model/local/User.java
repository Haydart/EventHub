package pl.rmakowiecki.eventhub.model.local;

import android.os.Parcel;
import android.os.Parcelable;

public final class User implements Parcelable {

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    private final String name;
    private final byte[] pictureData;

    public User(String name, byte[] pictureData) {
        this.name = name;
        this.pictureData = pictureData;
    }

    protected User(Parcel in) {
        name = in.readString();
        pictureData = in.createByteArray();
    }

    public String getName() {
        return name;
    }

    public byte[] getPicture() {
        return pictureData;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeByteArray(pictureData);
    }
}
