package pl.rmakowiecki.eventhub.ui.screen_user_profile;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.util.Collections;
import java.util.List;
import pl.rmakowiecki.eventhub.model.local.User;
import pl.rmakowiecki.eventhub.repository.QueryList;
import pl.rmakowiecki.eventhub.repository.Repository;
import pl.rmakowiecki.eventhub.repository.Specification;
import rx.Observable;

import static pl.rmakowiecki.eventhub.util.FirebaseConstants.USER_PROFILE_IMAGE_REFERENCE;

public class UserProfileRepository implements Repository<User>, QueryList<User> {

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
    public void add(User item) {
        add(Collections.singletonList(item));
    }

    @Override
    public void add(Iterable<User> items) {
        if (storageReference == null) {
            presenter.onProfileSaveFailure();
        }
        else {
            presenter.onProfileSaveProcessing();

            for (User item : items) {
                UploadTask uploadTask = storageReference.putBytes(item.getPicture());
                uploadTask.addOnFailureListener(exception -> {
                    presenter.onProfileSaveFailure();
                }).addOnSuccessListener(taskSnapshot -> {
                    presenter.onProfileSaveSuccess();
                });
            }
        }
    }

    @Override
    public void update(User item) {
        // TODO: 03.05.2017  
    }

    @Override
    public void remove(User item) {
        // TODO: 03.05.2017  
    }

    @Override
    public Observable<List<User>> query(Specification specification) {
        // TODO: 03.05.2017
        return null;
    }
}
