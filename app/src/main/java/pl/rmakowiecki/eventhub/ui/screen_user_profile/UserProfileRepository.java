package pl.rmakowiecki.eventhub.ui.screen_user_profile;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import pl.rmakowiecki.eventhub.model.local.UserProfile;
import pl.rmakowiecki.eventhub.repository.Repository;
import pl.rmakowiecki.eventhub.repository.Specification;
import rx.Observable;

import static pl.rmakowiecki.eventhub.util.FirebaseConstants.USER_PROFILE_IMAGE_REFERENCE;

public class UserProfileRepository implements Repository<UserProfile> {

    UserProfilePresenter presenter;
    private StorageReference storageReference;
    private FirebaseUser user;

    public UserProfileRepository(UserProfilePresenter profilePresenter, FirebaseUser firebaseUser) {
        user = firebaseUser;
        presenter = profilePresenter;
        if (user != null) {
            storageReference = FirebaseStorage.getInstance().getReference()
                    .child(USER_PROFILE_IMAGE_REFERENCE)
                    .child(user.getUid());
        }
    }

    @Override
    public void add(UserProfile item) {
        add(Collections.singletonList(item));
    }

    @Override
    public void add(Iterable<UserProfile> items) {
        if (storageReference == null) {
            presenter.onProfileSaveFailure();
        }
        else {
            presenter.onProfileSaveProcessing();

            for (UserProfile item : items) {
                UploadTask uploadTask = storageReference.putBytes(item.getPictureData());
                uploadTask.addOnFailureListener(exception -> {
                    presenter.onProfileSaveFailure();
                }).addOnSuccessListener(taskSnapshot -> {
                    presenter.onProfileSaveSuccess();
                });
            }
        }
    }

    @Override
    public void update(UserProfile item) {
        // TODO: 03.05.2017  
    }

    @Override
    public void remove(UserProfile item) {
        // TODO: 03.05.2017  
    }

    @Override
    public Observable<List<UserProfile>> query(Specification specification) {
        // TODO: 03.05.2017
        return null;
    }

    @Override
    public Observable<UserProfile> querySingle(Specification specification) {
        return Observable.empty();
    }
}
