package pl.rmakowiecki.eventhub.ui.screen_user_profile;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import pl.rmakowiecki.eventhub.api.UserImageStorageInteractor;
import pl.rmakowiecki.eventhub.api.UserProfileInteractor;
import pl.rmakowiecki.eventhub.model.local.User;
import pl.rmakowiecki.eventhub.repository.QuerySingle;
import pl.rmakowiecki.eventhub.repository.Repository;
import pl.rmakowiecki.eventhub.repository.Specification;
import rx.Observable;

public class UserProfileRepository implements Repository<User>, QuerySingle<User> {

    private FirebaseUser firebaseUser;
    private UserImageStorageInteractor imageStorageInteractor;
    private UserProfileInteractor userProfileDataInteractor;

    public UserProfileRepository() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        imageStorageInteractor = new UserImageStorageInteractor();
        userProfileDataInteractor = new UserProfileInteractor();
    }

    @Override
    public void add(User user) {
        userProfileDataInteractor.add(user.getName());
        imageStorageInteractor.add(user.getPicture());
    }

    @Override
    public void add(Iterable<User> items) {
        //no-op
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
    public Observable<User> querySingle(Specification specification) {
        return Observable.combineLatest(
                imageStorageInteractor.getData(),
                userProfileDataInteractor.getData(),
                (userPhoto, userName) -> new User(userName, userPhoto));
    }
}
