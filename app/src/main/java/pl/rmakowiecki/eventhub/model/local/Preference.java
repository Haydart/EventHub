package pl.rmakowiecki.eventhub.model.local;

import java.util.ArrayList;
import java.util.List;

import pl.rmakowiecki.eventhub.repository.DataItem;

public final class Preference implements DataItem {
    private final int id;
    private final String categoryName;
    private final List<String> subCategories;

    public Preference(int id, String categoryName) {
        this.id = id;
        this.categoryName = categoryName;
        subCategories = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return categoryName;
    }

    public void addSubcategory(String subcategory) {
        subCategories.add(subcategory);
    }

    public List<String> getSubCategories() {
        return subCategories;
    }
}
