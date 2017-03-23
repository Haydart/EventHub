package pl.rmakowiecki.eventhub.model.local;

import java.util.List;

import pl.rmakowiecki.eventhub.repository.DataItem;

public final class Preference implements DataItem {
    private final int id;
    private final String categoryName;
    private final List<String> subCategories;

    public Preference(int id, String categoryName, List<String> subCategories) {
        this.id = id;
        this.categoryName = categoryName;
        this.subCategories = subCategories;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return categoryName;
    }

    public List<String> getSubCategories() {
        return subCategories;
    }
}
