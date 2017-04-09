package pl.rmakowiecki.eventhub.ui.preferences_screen;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import java.util.ArrayList;
import java.util.List;

import pl.rmakowiecki.eventhub.R;


public class PreferenceInterestAdapter extends RecyclerView.Adapter<PreferenceInterestViewHolder> {
    private LayoutInflater layoutInflater;
    private Context context;
    private List<String> interestList;
    private List<PreferenceInterestViewHolder> holders;

    public PreferenceInterestAdapter(Context context, @NonNull List<String> interestList) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.interestList = interestList;
        holders = new ArrayList<>();
    }

    @Override
    public PreferenceInterestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.preference_list_child_item, parent, false);
        return new PreferenceInterestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PreferenceInterestViewHolder holder, int position) {
        holder.bindView(interestList.get(position));
        holders.add(holder);
    }

    @Override
    public int getItemCount() {
        return interestList.size();
    }

    public List<String> getCheckedSubCategories() {
        List<String> list = new ArrayList<>();
        for (PreferenceInterestViewHolder holder : holders)
            if (holder.isChecked())
                list.add(holder.getCategoryName());

        return list;
    }
}