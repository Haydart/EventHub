package pl.rmakowiecki.eventhub.ui.screen_preference_subcategories;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import pl.rmakowiecki.eventhub.R;

public class PreferenceInterestViewHolder extends ChildViewHolder {

    public static final int PREFERENCE_INTEREST_PADDING_TOP = 8;
    
    @BindView(R.id.preference_interest_list_item_text_view) TextView interestNameTextView;
    @BindView(R.id.preference_interest_list_item_checkbox) CheckBox interestInterestedCheckBox;

    private String nameText;

    public PreferenceInterestViewHolder(View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
        interestNameTextView.setPadding(0, PREFERENCE_INTEREST_PADDING_TOP, 0, 0);
    }

    public void bindView(String nameText, String localeNameText, boolean active) {
        this.nameText = nameText;
        interestNameTextView.setText(localeNameText.isEmpty() ? nameText : localeNameText);
        interestInterestedCheckBox.setChecked(active);
    }

    public String getCategoryName() {
        return nameText;
    }

    public boolean isChecked() {
        return interestInterestedCheckBox.isChecked();
    }
}
