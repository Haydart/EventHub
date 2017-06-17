package pl.rmakowiecki.eventhub.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import pl.rmakowiecki.eventhub.R;

public class AvatarPickDialogFragment extends DialogFragment {

    private Unbinder unbinder;

    @OnClick(R.id.camera_option_text_view)
    public void OnCameraOptionClicked() {
        tryNotifyActivity(AvatarPickDialogListener.AvatarSource.CAMERA);
    }

    @OnClick(R.id.gallery_option_text_view)
    public void OnGalleryOptionClicked() {
        tryNotifyActivity(AvatarPickDialogListener.AvatarSource.GALLERY);
    }

    private void tryNotifyActivity(AvatarPickDialogListener.AvatarSource avatarSource) {
        try {
            ((AvatarPickDialogListener) getActivity()).onDialogOptionChosen(avatarSource);
        } catch (ClassCastException ex) {
            Log.e(getClass().getSimpleName(), "Activity must implement AvatarPickDialogListener", ex);
        }
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
        if (unbinder != null) {
            unbinder.unbind();
        }
        super.onDestroyView();
    }

    public interface AvatarPickDialogListener {
        void onDialogOptionChosen(AvatarSource avatarSource);

        public enum AvatarSource {
            CAMERA,
            GALLERY
        }
    }
}
