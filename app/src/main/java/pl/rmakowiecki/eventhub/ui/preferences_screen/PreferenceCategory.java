package pl.rmakowiecki.eventhub.ui.preferences_screen;

import com.bignerdranch.expandablerecyclerview.model.Parent;

import java.util.ArrayList;
import java.util.List;

public class PreferenceCategory implements Parent<PreferenceInterest>
{
    private String title;
    private List<Object> childrenList;

    PreferenceCategory(String categoryTitle) {
        childrenList = new ArrayList<>();
        title = categoryTitle;
    }

    public String getTitle() {
        return title;
    }

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
