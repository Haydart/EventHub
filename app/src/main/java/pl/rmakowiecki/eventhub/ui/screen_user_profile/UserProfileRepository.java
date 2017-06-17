package pl.rmakowiecki.eventhub.ui.screen_user_profile;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import pl.rmakowiecki.eventhub.api.UserImageStorageInteractor;
import pl.rmakowiecki.eventhub.api.UserProfileInteractor;
import pl.rmakowiecki.eventhub.model.local.User;
import pl.rmakowiecki.eventhub.repository.QuerySingle;
import pl.rmakowiecki.eventhub.repository.QueryStatus;
import pl.rmakowiecki.eventhub.repository.Repository;
import pl.rmakowiecki.eventhub.repository.Specification;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

public class UserProfileRepository implements Repository<User, QueryStatus>, QuerySingle<User> {

    private FirebaseUser firebaseUser;
    private UserImageStorageInteractor imageStorageInteractor;
    private UserProfileInteractor userProfileDataInteractor;
    private PublishSubject<QueryStatus> publishSubject;

    public UserProfileRepository() {
        publishSubject = PublishSubject.create();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        imageStorageInteractor = new UserImageStorageInteractor();
        userProfileDataInteractor = new UserProfileInteractor();
    }

    @Override
    public Observable<QueryStatus> add(User user) {
        userProfileDataInteractor.add(user.getName())
                .compose(applySchedulers())
                .subscribe((result) -> saveDatabaseQueryResult(result));
        imageStorageInteractor.add(user.getPicture())
                .compose(applySchedulers())
                .subscribe((result) -> saveStorageQueryResult(result));

        return publishSubject;
    }

    private void saveDatabaseQueryResult(QueryStatus result) {
        publishSubject.onNext(result);
    }

    private void saveStorageQueryResult(QueryStatus result) {
        publishSubject.onNext(result);
    }

    protected <T> Observable.Transformer<T, T> applySchedulers() {
        return observable -> observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<QueryStatus> add(Iterable<User> items) {
        return Observable.empty();
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
        return firebaseUser != null ? Observable.combineLatest(
                imageStorageInteractor.getData(),
                userProfileDataInteractor.getData(),
                (userPhoto, userName) -> new User(userName, userPhoto))
                : Observable.empty();
    }
}
