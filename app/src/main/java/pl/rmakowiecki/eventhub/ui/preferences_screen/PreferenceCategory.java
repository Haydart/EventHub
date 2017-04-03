package pl.rmakowiecki.eventhub.ui.preferences_screen;

import android.os.Parcel;
import android.os.Parcelable;

import com.bignerdranch.expandablerecyclerview.model.Parent;
import java.util.ArrayList;
import java.util.List;

import pl.rmakowiecki.eventhub.model.local.Preference;

// TODO: 22.03.2017 Synchronize Preference with PreferenceCategory and PreferenceInterest
public class PreferenceCategory implements Parent<PreferenceInterest>, Parcelable {
    private final String title;
    private final String imageUrl;
    private List<String> childrenList;

    PreferenceCategory(String categoryTitle, String imgUrl) {
        childrenList = new ArrayList<>();
        title = categoryTitle;
        imageUrl = imgUrl.isEmpty() ? "InvalidUrl" : imgUrl;
    }

    PreferenceCategory(Parcel parcel){
        title = parcel.readString();
        String url = parcel.readString();
        childrenList = parcel.readArrayList(Object.class.getClassLoader());
        imageUrl = url.isEmpty() ? "InvalidUrl" : url;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void addChildString(String child) {
        childrenList.add(child);
    }

    @Override
    public List getChildList() {
        return childrenList;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }

    public static final Parcelable.Creator<PreferenceCategory> CREATOR
            = new Parcelable.Creator<PreferenceCategory>() {
        public PreferenceCategory createFromParcel(Parcel parcel) {
            return new PreferenceCategory(parcel);
        }

        public PreferenceCategory[] newArray(int size) {
            return new PreferenceCategory[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(imageUrl);
        dest.writeList(childrenList);
    }
}
