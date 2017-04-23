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

class PreferenceInterestAdapter extends RecyclerView.Adapter<PreferenceInterestViewHolder> {
    private LayoutInflater layoutInflater;
    private List<String> interestList;
    private List<PreferenceInterestViewHolder> holders;
    private Set<String> currentPreferences;

    public PreferenceInterestAdapter(Context context, @NonNull List<String> interestList, Set<String> currentPreferences) {
        layoutInflater = LayoutInflater.from(context);
        this.interestList = interestList;
        this.currentPreferences = currentPreferences;
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
        holder.bindView(subCategoryName, currentPreferences.contains(subCategoryName));
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