package pl.rmakowiecki.eventhub.model.local;

import java.util.List;

import pl.rmakowiecki.eventhub.repository.DataItem;

public final class Preference implements DataItem {
    private final int id;
    private final String categoryName;
    private final List<String> subCategories;
    private final String imageURL;

    public Preference(int id, String categoryName, List<String> subCategories, String imageURL) {
        this.id = id;
        this.categoryName = categoryName;
        this.subCategories = subCategories;
        this.imageURL = imageURL;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return categoryName;
    }

    public String getImageURL() { return imageURL; }

    public List<String> getSubCategories() {
        return subCategories;
    }
}
