package pl.rmakowiecki.eventhub.ui.screen_event_details;

import android.graphics.Bitmap;

import com.jenzz.noop.annotation.NoOp;
import pl.rmakowiecki.eventhub.ui.BaseView;

@NoOp
public interface EventDetailsView extends BaseView {
    void enableHomeButton();

    void initEventDetails();

    String getEventId();

    void displayEventPicture(Bitmap bitmapFromBytes);

    void initAttendeesList();

    void hideAttendeesList();

    void launchAppFeaturesActivity();

    void updateJoinEventButton(boolean isUserAttendingEvent);

    void showFailureMessage();

    void showButtonProcessing();

    void showSuccessMessage();
}
