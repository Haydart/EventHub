package pl.rmakowiecki.eventhub.ui.screen_preference_categories;

import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bignerdranch.expandablerecyclerview.ParentViewHolder;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import pl.rmakowiecki.eventhub.R;
import pl.rmakowiecki.eventhub.util.LocaleUtils;
import pl.rmakowiecki.eventhub.util.PreferencesManager;

import static pl.rmakowiecki.eventhub.util.FirebaseConstants.PL_LOCALE_REFERENCE;

class PreferenceCategoryViewHolder extends ParentViewHolder {

    public static final int ANIMATION_START_OFFSET = 200;
    @BindView(R.id.preference_category_list_item_image_view) ImageView categoryImageView;
    @BindView(R.id.preference_category_list_checked_image_view) ImageView checkImageView;
    @BindView(R.id.preference_category_list_item_category_name) TextView categoryNameView;
    @BindView(R.id.preference_category_list_item_progress_bar) ProgressBar categoryProgressBar;
    @BindView(R.id.preference_category_list_item_dark_view) View darkView;

    private final String resourceSource = "drawable";

    private View view;
    private PreferenceCategory category;
    private PreferenceItemListener itemListener;

    PreferenceCategoryViewHolder(View itemView, PreferenceItemListener listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        view = itemView;
        itemListener = listener;
    }

    @OnClick(R.id.preference_category_list_item_image_view)
    void onImageClick(ImageView image) {
        itemListener.onImageClick(image, category);
    }

    private void loadCheck() {
        Picasso.with(view.getContext())
                .load(R.drawable.ic_check_circle_white_48dp)
                .fit()
                .into(checkImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        checkImageView.setVisibility(View.VISIBLE);
                        animateCheckImage();
                    }

                    @Override
                    public void onError() {
                        // TODO: 2017-04-27
                    }
                });
    }

    private void animateCheckImage() {
        Animation scaleUpFadeInAnimation = AnimationUtils.loadAnimation(view.getContext(), R.anim.scale_up_fade_in);
        scaleUpFadeInAnimation.setStartOffset(ANIMATION_START_OFFSET);
        checkImageView.startAnimation(scaleUpFadeInAnimation);
    }

    void bindView(PreferenceCategory category, boolean isSelected) {
        darkView.setVisibility(View.INVISIBLE);
        checkImageView.setVisibility(View.INVISIBLE);
        this.category = category;

        String categoryName = category.getTitle();
        String localeName = new LocaleUtils().getLocaleString();
        PreferencesManager manager = new PreferencesManager(view.getContext());
        if (!localeName.isEmpty())
            categoryName = manager.getNameOrLocaleName(localeName, categoryName, categoryName);
        categoryNameView.setText(categoryName);

        int resourceID = view.getContext()
                .getResources()
                .getIdentifier(category.getImageResourceName(), resourceSource, view.getContext().getPackageName());

        if (resourceID != 0) {
            Picasso.with(view.getContext())
                    .load(resourceID)
                    .fit()
                    .into(categoryImageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            if (isSelected) {
                                darkView.setVisibility(View.VISIBLE);
                                loadCheck();
                            }

                            categoryProgressBar.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onError() {
                            // TODO: 23/04/2017 display some error image
                        }
                    });
        }

        ViewCompat.setTransitionName(categoryImageView, String.valueOf(category.getTitle()));
    }
}
