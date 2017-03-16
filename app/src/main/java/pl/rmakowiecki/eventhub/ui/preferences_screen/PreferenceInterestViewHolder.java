package pl.rmakowiecki.eventhub.ui.preferences_screen;


import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.rmakowiecki.eventhub.R;

public class PreferenceInterestViewHolder extends ChildViewHolder {

    public static final int PREFERENCE_INTEREST_PADDING_TOP = 8;
    
    @BindView(R.id.preference_interest_list_item_text_view)TextView interestNameTextView;
    @BindView(R.id.preference_interest_list_item_checkbox)CheckBox interestInterestedCheckBox;

    public PreferenceInterestViewHolder(View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
        interestNameTextView.setPadding(0, PREFERENCE_INTEREST_PADDING_TOP, 0, 0);
    }
}
