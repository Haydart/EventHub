package pl.rmakowiecki.eventhub.ui.preferences_screen;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ParentViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.rmakowiecki.eventhub.R;

public class PreferenceCategoryViewHolder extends ParentViewHolder {

    @BindView(R.id.preference_category_list_item_text_view)ImageView categoryNameTextView;

    public PreferenceCategoryViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
