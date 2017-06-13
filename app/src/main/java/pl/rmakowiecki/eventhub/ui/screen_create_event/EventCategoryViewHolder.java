package pl.rmakowiecki.eventhub.ui.screen_create_event;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import pl.rmakowiecki.eventhub.R;
import pl.rmakowiecki.eventhub.ui.screen_preference_categories.PreferenceCategory;

class EventCategoryViewHolder extends RecyclerView.ViewHolder {

    private static final long ANIMATION_START_OFFSET = 200;

    @BindView(R.id.event_category_image) ImageView eventCategoryImageView;
    @BindView(R.id.check_image_view) ImageView checkImageView;
    @BindView(R.id.event_category_name_text_view) TextView eventCategoryNameTextView;

    private final String resourceSource = "drawable";
    private final View view;
    private final EventCategoryClickListener itemClickListener;

    EventCategoryViewHolder(View itemView, EventCategoryClickListener itemListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        view = itemView;
        this.itemClickListener = itemListener;
    }

    public void bindView(String categoryName, PreferenceCategory category, boolean isSelected) {
        setupClickListener(category);
        eventCategoryNameTextView.setText(categoryName);
        loadCategoryImage(category, isSelected);
    }

    private void loadCategoryImage(PreferenceCategory category, final boolean isSelected) {
        int resourceID = view.getContext()
                .getResources()
                .getIdentifier(category.getImageResourceName(), resourceSource, view.getContext().getPackageName());

        if (resourceID != 0) {
            Picasso.with(view.getContext())
                    .load(resourceID)
                    .fit()
                    .into(eventCategoryImageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            if (isSelected) {
                                showCheckMark();
                            }
                        }

                        @Override
                        public void onError() {
                            // TODO: 23/04/2017 display some error image
                        }
                    });
        }
    }

    private void setupClickListener(PreferenceCategory category) {
        view.setOnClickListener(v -> itemClickListener.onCategoryClicked(getAdapterPosition(), category));
    }

    private void showCheckMark() {
        Picasso.with(view.getContext())
                .load(R.drawable.ic_check_circle_white_48dp)
                .fit()
                .into(checkImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        checkImageView.setVisibility(View.VISIBLE);
                        animateCheckImageDelayed();
                    }

                    @Override
                    public void onError() {
                        // TODO: 2017-04-27
                    }
                });
    }

    private void animateCheckImageDelayed() {
        Animation scaleUpFadeInAnimation = AnimationUtils.loadAnimation(view.getContext(), R.anim.scale_up_fade_in);
        scaleUpFadeInAnimation.setStartOffset(ANIMATION_START_OFFSET);
        checkImageView.startAnimation(scaleUpFadeInAnimation);
    }
}
