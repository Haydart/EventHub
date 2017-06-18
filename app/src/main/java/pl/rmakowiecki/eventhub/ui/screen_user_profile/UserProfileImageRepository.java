package pl.rmakowiecki.eventhub.ui.screen_user_profile;

import java.util.HashMap;
import java.util.Map;

import pl.rmakowiecki.eventhub.api.UserImageStorageInteractor;
import pl.rmakowiecki.eventhub.repository.AddSpecificOperationRepository;
import pl.rmakowiecki.eventhub.repository.GenericQueryStatus;
import pl.rmakowiecki.eventhub.repository.QuerySingle;
import pl.rmakowiecki.eventhub.repository.Specification;
import rx.Observable;
import rx.subjects.PublishSubject;

public class UserProfileImageRepository implements AddSpecificOperationRepository<byte[], GenericQueryStatus>, QuerySingle<byte[]> {

    UserImageStorageInteractor userImageStorageInteractor = new UserImageStorageInteractor();
    Map<String, byte[]> userImageMap = new HashMap<>();
    PublishSubject<byte[]> publishSubject = PublishSubject.create();

    public byte[] getImage(Specification specification) {
        UserProfileImageSpecification userImageSpecification = (UserProfileImageSpecification) specification;
        String userId = userImageSpecification.getFirebaseKey();
        if (!userId.isEmpty() && userImageMap.containsKey(userId))
            return userImageMap.get(userId);

        return null;
    }

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
        if (!userImageMap.containsKey(key))
            userImageMap.put(key, item);
        return Observable.just(GenericQueryStatus.STATUS_SUCCESS);
    }

    @Override
    public Observable<GenericQueryStatus> add(String key, Iterable<byte[]> items) {
        return Observable.empty();
    }
}
