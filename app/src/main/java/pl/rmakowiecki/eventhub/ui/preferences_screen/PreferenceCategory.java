package pl.rmakowiecki.eventhub.ui.preferences_screen;

import com.bignerdranch.expandablerecyclerview.model.Parent;

import java.util.ArrayList;
import java.util.List;

// TODO: 22.03.2017 Synchronize Preference with PreferenceCategory and PreferenceInterest
public class PreferenceCategory implements Parent<PreferenceInterest> {
    private final String title;
    private final String imageURL;
    private List<Object> childrenList;

    PreferenceCategory(String categoryTitle, String imgURL) {
        childrenList = new ArrayList<>();
        title = categoryTitle;
        imageURL = imgURL;
    }

    public String getTitle() {
        return title;
    }

    public String getImageURL() { return imageURL; }

    public void addChildObject(Object child) {
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
}
