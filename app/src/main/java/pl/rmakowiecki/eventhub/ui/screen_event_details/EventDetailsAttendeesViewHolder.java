package pl.rmakowiecki.eventhub.ui.screen_event_details;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ParentViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.rmakowiecki.eventhub.R;
import pl.rmakowiecki.eventhub.util.BitmapUtils;

public class EventDetailsAttendeesViewHolder extends ParentViewHolder {

    @BindView(R.id.attending_user_image_view) ImageView attendeeImageView;
    @BindView(R.id.attending_user_text_view) TextView attendeeTextView;

    public EventDetailsAttendeesViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindView(String attendeeName, byte[] attendeePicture) {
        attendeeTextView.setText(attendeeName);

        if (attendeePicture != null) {
            Bitmap picture = BitmapUtils.getBitmapFromBytes(attendeePicture);
            if (picture != null)
                attendeeImageView.setImageBitmap(picture);
        }
    }
}
