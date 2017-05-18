package pl.rmakowiecki.eventhub.ui.screen_preference_categories;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pl.rmakowiecki.eventhub.R;
import pl.rmakowiecki.eventhub.util.PreferencesManager;

public class PreferenceAdapter extends RecyclerView.Adapter<PreferenceCategoryViewHolder> {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<PreferenceCategory> items;
    private PreferenceItemListener itemListener;
    private SharedPreferences sharedPreferences;

    public PreferenceAdapter(Context appContext, @NonNull List<PreferenceCategory> parentItemList, PreferenceItemListener listener, SharedPreferences preferences) {
        context = appContext;
        layoutInflater = LayoutInflater.from(context);
        items = parentItemList;
        itemListener = listener;
        sharedPreferences = preferences;
    }

    @Override
    public PreferenceCategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.preference_card_layout, parent, false);
        return new PreferenceCategoryViewHolder(view, itemListener);
    }

    @Override
    public void onBindViewHolder(PreferenceCategoryViewHolder holder, int position) {
        PreferenceCategory category = items.get(position);
        holder.bindView(category, isSelected(category));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private boolean isSelected(PreferenceCategory category) {
        Set<String> subCategories = new PreferencesManager(context).getInterests(category.getTitle());
        return !subCategories.isEmpty();
}
}
