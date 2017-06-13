
package pl.rmakowiecki.eventhub.ui.screen_create_event;

import pl.rmakowiecki.eventhub.ui.screen_preference_categories.PreferenceCategory;

interface EventCategoryClickListener {
    void onCategoryClicked(int position, PreferenceCategory category);
}
