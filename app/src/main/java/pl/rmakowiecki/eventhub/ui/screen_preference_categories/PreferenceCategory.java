package pl.rmakowiecki.eventhub.ui.screen_preference_categories;

import android.os.Parcel;
import android.os.Parcelable;
import com.bignerdranch.expandablerecyclerview.model.Parent;
import java.util.List;
import pl.rmakowiecki.eventhub.ui.screen_preference_subcategories.PreferenceInterest;

// TODO: 22.03.2017 Synchronize Preference with PreferenceCategory and PreferenceInterest
public class PreferenceCategory implements Parent<PreferenceInterest>, Parcelable {
    private final String title;
    private final String imageResourceName;
    private List<String> childrenList;

    public PreferenceCategory(String categoryTitle, String imgResourceName, List<String> childList) {
        childrenList = childList;
        title = categoryTitle;
        imageResourceName = imgResourceName.isEmpty() ? "InvalidName" : imgResourceName;
    }

    PreferenceCategory(Parcel parcel) {
        title = parcel.readString();
        String resourceName = parcel.readString();
        childrenList = parcel.readArrayList(Object.class.getClassLoader());
        imageResourceName = resourceName.isEmpty() ? "InvalidName" : resourceName;
    }

    public String getTitle() {
        return title;
    }

    public String getImageResourceName() {
        return imageResourceName;
    }

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
        dest.writeString(imageResourceName);
        dest.writeList(childrenList);
    }
}
