package pl.rmakowiecki.eventhub.ui.preferences_screen;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;

import java.util.List;

import pl.rmakowiecki.eventhub.R;

public class PreferenceAdapter extends ExpandableRecyclerAdapter<PreferenceCategory,
        PreferenceInterest, PreferenceCategoryViewHolder, PreferenceInterestViewHolder> {

    LayoutInflater layoutInflater;

    public PreferenceAdapter(Context context, @NonNull List<PreferenceCategory> parentItemList) {
        super(parentItemList);
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public PreferenceCategoryViewHolder onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {
        View view = layoutInflater.inflate(R.layout.preference_list_parent_item, parentViewGroup, false);
        return new PreferenceCategoryViewHolder(view);
    }

    @NonNull
    @Override
    public PreferenceInterestViewHolder onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {
        View view = layoutInflater.inflate(R.layout.preference_list_child_item, childViewGroup, false);
        return new PreferenceInterestViewHolder(view);
    }

    @Override
    public void onBindParentViewHolder(@NonNull PreferenceCategoryViewHolder parentViewHolder, int parentPosition, @NonNull PreferenceCategory category) {
        parentViewHolder.categoryNameTextView.setText(category.getTitle());
    }

    @Override
    public void onBindChildViewHolder(@NonNull PreferenceInterestViewHolder childViewHolder, int parentPosition, int childPosition, @NonNull PreferenceInterest interest) {
        childViewHolder.interestNameTextView.setText(interest.getTitle());
        childViewHolder.interestInterestedCheckBox.setChecked(interest.isInterested());
    }
}
