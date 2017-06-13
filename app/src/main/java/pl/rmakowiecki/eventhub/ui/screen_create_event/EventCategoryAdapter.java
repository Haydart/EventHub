package pl.rmakowiecki.eventhub.ui.screen_create_event;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;
import java.util.Set;
import pl.rmakowiecki.eventhub.R;
import pl.rmakowiecki.eventhub.ui.screen_preference_categories.PreferenceCategory;
import pl.rmakowiecki.eventhub.util.LocaleUtils;
import pl.rmakowiecki.eventhub.util.PreferencesManager;

public class EventCategoryAdapter extends RecyclerView.Adapter<EventCategoryViewHolder> {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<PreferenceCategory> items;
    private EventCategoryClickListener itemListener;
    private PreferencesManager preferenceManager;

    public EventCategoryAdapter(Context appContext, @NonNull List<PreferenceCategory> parentItemList, EventCategoryClickListener listener) {
        context = appContext;
        layoutInflater = LayoutInflater.from(context);
        items = parentItemList;
        itemListener = listener;
        preferenceManager = new PreferencesManager(context);
    }

    @Override
    public EventCategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.event_category_pick_layout, parent, false);
        return new EventCategoryViewHolder(view, itemListener);
    }

    @Override
    public void onBindViewHolder(EventCategoryViewHolder holder, int position) {
        PreferenceCategory category = items.get(position);
        String categoryName = category.getTitle();
        String localeName = new LocaleUtils().getLocaleString();
        if (!localeName.isEmpty()) {
            categoryName = preferenceManager.getNameOrLocaleName(localeName, categoryName, categoryName);
        }
        holder.bindView(categoryName, category, isSelected(category));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private boolean isSelected(PreferenceCategory category) {
        Set<String> subCategories = preferenceManager.getInterests(category.getTitle());
        return !subCategories.isEmpty();
    }
}
