package pl.rmakowiecki.eventhub.ui.screen_user_profile;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ParentViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.rmakowiecki.eventhub.R;

public class UserProfileCategoryViewHolder extends ParentViewHolder{

    @BindView(R.id.user_profile_preference_category_list_item_text_view) TextView categoryNameView;

    public UserProfileCategoryViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
