package pl.rmakowiecki.eventhub.ui.preferences_screen;

import android.view.View;
import android.widget.ImageView;

import com.bignerdranch.expandablerecyclerview.ParentViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.rmakowiecki.eventhub.R;

public class PreferenceCategoryViewHolder extends ParentViewHolder {

    @BindView(R.id.preference_category_list_item_image_view) ImageView categoryImageView;

    private PreferenceCategory category;

    public PreferenceCategoryViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @OnClick(R.id.preference_category_list_item_image_view)
    public void onImageClick(View v) {
        ((PreferenceActivity)v.getContext()).handlePreferenceImageClick(category);
    }

    public void setImageCategory(PreferenceCategory category) {
        this.category = category;
    }
}
