package pl.rmakowiecki.eventhub.model.local;

import java.util.List;

public class Interest {
        private final String categoryName;
        private final List<String> subCategories;

        public Interest(String categoryName, List<String> subCategories) {
            this.categoryName = categoryName;
            this.subCategories = subCategories;
        }

        public String getName() {
            return categoryName;
        }

        public List<String> getSubCategories() {
            return subCategories;
        }
}
