package pl.rmakowiecki.eventhub.ui.screen_user_profile;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.rmakowiecki.eventhub.R;

public class UserProfileSubCategoryViewHolder extends ChildViewHolder<String> {

    @BindView(R.id.user_profile_preference_interest_list_item_text_view) TextView subcategoryNameView;

    public UserProfileSubCategoryViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
