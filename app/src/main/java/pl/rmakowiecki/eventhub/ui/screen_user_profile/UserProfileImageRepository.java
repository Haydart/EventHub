package pl.rmakowiecki.eventhub.ui.screen_user_profile;

import pl.rmakowiecki.eventhub.api.UserImageStorageInteractor;
import pl.rmakowiecki.eventhub.repository.QuerySingle;
import pl.rmakowiecki.eventhub.repository.Repository;
import pl.rmakowiecki.eventhub.repository.Specification;
import rx.Observable;

public class UserProfileImageRepository implements Repository<byte[]>, QuerySingle<byte[]> {

    @Override
    public void add(byte[] item) {
        //no-op
    }

    @Override
    public void add(Iterable<byte[]> items) {
        //no-op
    }

    @Override
    public void update(byte[] item) {
        //no-op
    }

    @Override
    public void remove(byte[] item) {
        //no-op
    }

    @Override
    public Observable<byte[]> querySingle(Specification specification) {
        return new UserImageStorageInteractor().getData();
    }
}
