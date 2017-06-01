package pl.rmakowiecki.eventhub.ui.screen_user_profile;

import java.util.List;

import pl.rmakowiecki.eventhub.api.UserImageStorageInteractor;
import pl.rmakowiecki.eventhub.repository.Repository;
import pl.rmakowiecki.eventhub.repository.Specification;
import rx.Observable;

public class UserProfileImageRepository implements Repository<byte[]> {

    @Override
    public void add(byte[] item) {

    }

    @Override
    public void add(Iterable<byte[]> items) {

    }

    @Override
    public void update(byte[] item) {

    }

    @Override
    public void remove(byte[] item) {

    }

    @Override
    public Observable<List<byte[]>> query(Specification specification) {
        return null;
    }

    @Override
    public Observable<byte[]> querySingle(Specification specification) {
        return new UserImageStorageInteractor().getData();
    }
}
