package pl.rmakowiecki.eventhub.ui.screen_preference_subcategories;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import pl.rmakowiecki.eventhub.R;
import pl.rmakowiecki.eventhub.util.PreferencesManager;

class PreferenceInterestAdapter extends RecyclerView.Adapter<PreferenceInterestViewHolder> {
    private LayoutInflater layoutInflater;
    private List<String> interestList;
    private List<PreferenceInterestViewHolder> holders;
    private Set<String> currentPreferences;
    private PreferencesManager preferencesManager;
    private String localeString;
    private String categoryName;

    public PreferenceInterestAdapter(Context context, @NonNull List<String> interestList, Set<String> currentPreferences, PreferencesManager manager,
                                     String localeString, String categoryName) {
        layoutInflater = LayoutInflater.from(context);
        preferencesManager = manager;
        this.interestList = interestList;
        this.currentPreferences = currentPreferences;
        this.localeString = localeString;
        this.categoryName = categoryName;
        holders = new ArrayList<>();
    }

    @Override
    public PreferenceInterestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.preference_list_child_item, parent, false);
        return new PreferenceInterestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PreferenceInterestViewHolder holder, int position) {
        String subCategoryName = interestList.get(position);
        String localeCategoryName = subCategoryName;
        if (!localeString.isEmpty()) {
            localeCategoryName = preferencesManager.getNameOrLocaleName(localeString, categoryName, subCategoryName);
        }
        holder.bindView(subCategoryName, localeCategoryName, currentPreferences.contains(subCategoryName));
        holders.add(holder);
    }

    @Override
    public int getItemCount() {
        return interestList.size();
    }

    List<String> getCheckedSubCategories() {
        List<String> list = new ArrayList<>();
        for (PreferenceInterestViewHolder holder : holders)
            if (holder.isChecked())
                list.add(holder.getCategoryName());

        return list;
    }
}