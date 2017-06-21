package pl.rmakowiecki.eventhub.ui.screen_user_profile;

import pl.rmakowiecki.eventhub.api.UserImageStorageInteractor;
import pl.rmakowiecki.eventhub.repository.AddSpecificOperationRepository;
import pl.rmakowiecki.eventhub.repository.GenericQueryStatus;
import pl.rmakowiecki.eventhub.repository.QuerySingle;
import pl.rmakowiecki.eventhub.repository.Specification;
import rx.Observable;
import rx.subjects.PublishSubject;

public class UserProfileImageRepository implements AddSpecificOperationRepository<byte[], GenericQueryStatus>, QuerySingle<byte[]> {

    UserImageStorageInteractor userImageStorageInteractor = new UserImageStorageInteractor();
    PublishSubject<byte[]> publishSubject = PublishSubject.create();

    @Override
    public Observable<byte[]> querySingle(Specification specification) {
        UserProfileImageSpecification userImageSpecification = (UserProfileImageSpecification) specification;
        String userId = userImageSpecification.getFirebaseKey();

        userImageStorageInteractor
                .getData(userId)
                .subscribe(data -> dataArrived(data));

        return publishSubject;
    }

    private void dataArrived(byte[] data) {
        publishSubject.onNext(data);
    }

    @Override
    public Observable<GenericQueryStatus> add(String key, byte[] item) {
        return Observable.empty();
    }

    @Override
    public Observable<GenericQueryStatus> add(String key, Iterable<byte[]> items) {
        return Observable.empty();
    }
}
