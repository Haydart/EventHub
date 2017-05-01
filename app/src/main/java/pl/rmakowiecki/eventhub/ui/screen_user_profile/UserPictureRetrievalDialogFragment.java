package pl.rmakowiecki.eventhub.ui.screen_user_profile;


import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import pl.rmakowiecki.eventhub.R;

public class UserPictureRetrievalDialogFragment extends DialogFragment {

    private static final int PHOTO_SOURCE_CAMERA = 1;
    private static final int PHOTO_SOURCE_GALLERY = 2;

    private Unbinder unbinder;

    @OnClick(R.id.cameraOptionTextView)
    public void OnCameraOptionClicked() {
        ((UserProfileActivity)
                getActivity()).onDialogFragmentButtonClick(PHOTO_SOURCE_CAMERA);
    }

    @OnClick(R.id.galleryOptionTextView)
    public void OnGalleryOptionClicked() {
        ((UserProfileActivity)
                getActivity()).onDialogFragmentButtonClick(PHOTO_SOURCE_GALLERY);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.user_profile_picture_fragment, null, false);
        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).setView(rootView).create();
        unbinder = ButterKnife.bind(this, rootView);

        return alertDialog;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }
}
