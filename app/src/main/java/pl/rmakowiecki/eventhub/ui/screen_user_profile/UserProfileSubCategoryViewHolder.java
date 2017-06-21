package pl.rmakowiecki.eventhub.ui.screen_user_profile;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import pl.rmakowiecki.eventhub.R;

class UserProfileSubCategoryViewHolder extends ChildViewHolder<String> {

    @BindView(R.id.user_profile_preference_interest_list_item_text_view) TextView subcategoryNameView;

    UserProfileSubCategoryViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
