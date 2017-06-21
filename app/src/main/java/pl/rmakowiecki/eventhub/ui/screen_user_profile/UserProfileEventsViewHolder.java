package pl.rmakowiecki.eventhub.ui.screen_user_profile;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ParentViewHolder;

import org.joda.time.DateTime;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.rmakowiecki.eventhub.R;
import pl.rmakowiecki.eventhub.model.local.Event;
import pl.rmakowiecki.eventhub.util.DateUtils;

class UserProfileEventsViewHolder extends ParentViewHolder {

    @BindView(R.id.event_frame_layout) FrameLayout eventFrameLayout;
    @BindView(R.id.event_image_view) ImageView eventImageView;
    @BindView(R.id.event_name_text_view) TextView eventNameTextView;
    @BindView(R.id.event_address_text_view) TextView eventAddressTextView;
    @BindView(R.id.event_day_text_view) TextView eventDayTextView;
    @BindView(R.id.event_month_text_view) TextView eventMonthTextView;

    UserProfileEventsViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindView(Event event) {
        eventNameTextView.setText(event.getName());
        eventAddressTextView.setText(event.getAddress());
        DateTime dateOfEvent = new DateTime(event.getTimestamp());
        eventDayTextView.setText(DateUtils.getFormattedDate(dateOfEvent, "dd"));
        eventMonthTextView.setText(DateUtils.getFormattedDate(dateOfEvent, "MMM").toUpperCase());
    }

    void loadPicture(Bitmap eventPicture) {
        eventImageView.setImageBitmap(eventPicture);
    }
}
