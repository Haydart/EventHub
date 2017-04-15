package pl.rmakowiecki.eventhub.ui.preferences_screen;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ParentViewHolder;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.rmakowiecki.eventhub.R;

public class PreferenceCategoryViewHolder extends ParentViewHolder {

    @BindView(R.id.preference_category_list_item_image_view) ImageView categoryImageView;
    @BindView(R.id.preference_category_list_item_category_name) TextView categoryNameView;
    @BindView(R.id.preference_category_list_item_progress_bar) ProgressBar categoryProgressBar;

    private final String resourceSource = "drawable";

    private View view;
    private PreferenceCategory category;
    private PreferenceItemListener itemListener;

    public PreferenceCategoryViewHolder(View itemView, PreferenceItemListener listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        view = itemView;
        itemListener = listener;
    }

    @OnClick(R.id.preference_category_list_item_image_view)
    public void onImageClick(View v) {
        itemListener.onImageClick(category);
    }

    public void bindView(PreferenceCategory category) {
        this.category = category;
        categoryNameView.setText(category.getTitle());

        int resourceID = view
                        .getContext()
                        .getResources()
                        .getIdentifier(category.getImageResourceName(), resourceSource, view.getContext().getPackageName());

        Picasso
                .with(view.getContext())
                .load(resourceID != 0 ? resourceID : R.drawable.ic_image_placeholder)
                .fit()
                .into(categoryImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        categoryProgressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onError() {
                        // no-op
                    }
                });
    }
}
