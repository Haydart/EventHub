package pl.rmakowiecki.eventhub.ui.preferences_screen;


import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.rmakowiecki.eventhub.R;

public class PreferenceInterestViewHolder extends ChildViewHolder {

    @BindView(R.id.preference_interest_list_item_text_view)TextView activityNameTextView;
    @BindView(R.id.preference_interest_list_item_checkbox)CheckBox activityInterestedCheckBox;

    public PreferenceInterestViewHolder(View itemView)
    {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }
}
