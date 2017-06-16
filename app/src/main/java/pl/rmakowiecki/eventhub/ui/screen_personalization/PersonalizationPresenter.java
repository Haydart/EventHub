package pl.rmakowiecki.eventhub.ui.screen_personalization;

import pl.rmakowiecki.eventhub.ui.AvatarPickDialogFragment;
import pl.rmakowiecki.eventhub.ui.BasePresenter;

class PersonalizationPresenter extends BasePresenter<PersonalizationView> {

    void onChooseImageButtonClicked() {
        view.showPictureSelectDialog();
    }

    @Override
    public PersonalizationView getNoOpView() {
        return NoOpPersonalizationView.INSTANCE;
    }

    void onPhotoOptionSelected(AvatarPickDialogFragment.AvatarPickDialogListener.AvatarSource photoSource) {
        if (photoSource == AvatarPickDialogFragment.AvatarPickDialogListener.AvatarSource.CAMERA) {
            view.launchCameraAppIntent();
        } else if (photoSource == AvatarPickDialogFragment.AvatarPickDialogListener.AvatarSource.GALLERY) {
            view.launchGalleryAppIntent();
        }
    }
}
