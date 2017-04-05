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

    LayoutInflater layoutInflater;
    Context context;
    List<PreferenceCategory> items;

    public PreferenceAdapter(Context context, @NonNull List<PreferenceCategory> parentItemList) {
        super();
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        items = parentItemList;
    }

    @Override
    public PreferenceCategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.preference_card_layout, parent, false);
        return new PreferenceCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PreferenceCategoryViewHolder holder, int position) {
        holder.setImageCategory(items.get(position));

        Picasso
                .with(context)
                .load(items.get(position).getImageUrl())
                .placeholder(R.drawable.ic_image_placeholder)
                .into(holder.categoryImageView);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
