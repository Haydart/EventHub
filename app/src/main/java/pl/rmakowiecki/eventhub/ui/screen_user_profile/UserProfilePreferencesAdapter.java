package pl.rmakowiecki.eventhub.ui.screen_user_profile;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;

import java.util.List;

import pl.rmakowiecki.eventhub.R;
import pl.rmakowiecki.eventhub.ui.screen_preference_categories.PreferenceCategory;

public class UserProfilePreferencesAdapter extends ExpandableRecyclerAdapter<PreferenceCategory,
        String, UserProfileCategoryViewHolder, UserProfileSubCategoryViewHolder> {

    private LayoutInflater layoutInflater;

    public UserProfilePreferencesAdapter(Context context, @NonNull List<PreferenceCategory> parentList) {
        super(parentList);
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public UserProfileCategoryViewHolder onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {
        View view = layoutInflater.inflate(R.layout.user_profile_preference_list_parent, parentViewGroup, false);
        return new UserProfileCategoryViewHolder(view);
    }

    @NonNull
    @Override
    public UserProfileSubCategoryViewHolder onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {
        View view = layoutInflater.inflate(R.layout.user_profile_preference_list_child, childViewGroup, false);
        return new UserProfileSubCategoryViewHolder(view);
    }

    @Override
    public void onBindParentViewHolder(@NonNull UserProfileCategoryViewHolder parentViewHolder, int parentPosition, @NonNull PreferenceCategory category) {
        parentViewHolder.categoryNameView.setText(category.getTitle());
    }

    @Override
    public void onBindChildViewHolder(@NonNull UserProfileSubCategoryViewHolder childViewHolder, int parentPosition, int childPosition, @NonNull String interest) {
        childViewHolder.subcategoryNameView.setText(interest);
    }
}
