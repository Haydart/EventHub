package pl.rmakowiecki.eventhub.ui.screen_personalization;

import com.jenzz.noop.annotation.NoOp;
import pl.rmakowiecki.eventhub.ui.BaseView;

@NoOp
interface PersonalizationView extends BaseView {
    void showPictureSelectDialog();

    void launchCameraAppIntent();

    void launchGalleryAppIntent();
}
