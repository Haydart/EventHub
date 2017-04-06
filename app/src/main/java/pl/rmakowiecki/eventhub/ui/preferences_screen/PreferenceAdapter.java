package pl.rmakowiecki.eventhub.ui.preferences_screen;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

import pl.rmakowiecki.eventhub.R;

public class PreferenceAdapter extends RecyclerView.Adapter<PreferenceCategoryViewHolder> {

    private LayoutInflater layoutInflater;
    private Context context;
    private List<PreferenceCategory> items;
    private PreferenceItemListener itemListener;

    public PreferenceAdapter(Context context, @NonNull List<PreferenceCategory> parentItemList, PreferenceItemListener listener) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        items = parentItemList;
        itemListener = listener;
    }

    @Override
    public PreferenceCategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.preference_card_layout, parent, false);
        return new PreferenceCategoryViewHolder(view, itemListener);
    }

    @Override
    public void onBindViewHolder(PreferenceCategoryViewHolder holder, int position) {
        holder.bindView(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
