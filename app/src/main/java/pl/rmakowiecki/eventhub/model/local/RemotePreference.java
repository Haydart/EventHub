package pl.rmakowiecki.eventhub.model.local;

import java.util.List;

public final class RemotePreference {
    private final int id;
    private final String categoryName;
    private final List<String> subCategories;
    private final String imageUrl;

    public RemotePreference(int id, String categoryName, List<String> subCategories, String imageUrl) {
        this.id = id;
        this.categoryName = categoryName;
        this.subCategories = subCategories;
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return categoryName;
    }

    public String getImageUrl() { return imageUrl; }

    public List<String> getSubCategories() {
        return subCategories;
    }
}
