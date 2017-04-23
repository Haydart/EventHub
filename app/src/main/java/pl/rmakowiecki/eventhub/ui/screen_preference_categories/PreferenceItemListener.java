package pl.rmakowiecki.eventhub.ui.screen_preference_categories;

import android.widget.ImageView;

interface PreferenceItemListener {
    void onImageClick(ImageView image, PreferenceCategory category);
}
