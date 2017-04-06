package pl.rmakowiecki.eventhub.ui.preferences_screen;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import java.util.List;

import pl.rmakowiecki.eventhub.R;


public class PreferenceInterestAdapter extends RecyclerView.Adapter<PreferenceInterestViewHolder> {
    private LayoutInflater layoutInflater;
    private Context context;
    private List<String> interestList;

    public PreferenceInterestAdapter(Context context, @NonNull List<String> interestList) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.interestList = interestList;
    }

    @Override
    public PreferenceInterestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.preference_list_child_item, parent, false);
        return new PreferenceInterestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PreferenceInterestViewHolder holder, int position) {
        holder.bindView(interestList.get(position));
    }

    @Override
    public int getItemCount() {
        return interestList.size();
    }
}