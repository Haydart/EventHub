package pl.rmakowiecki.eventhub.repository;

import java.util.List;
import pl.rmakowiecki.eventhub.model.local.Preference;
import rx.Observable;

public class PreferencesRepository implements Repository<Preference> {

    @Override
    public void add(Preference item) {

    }

    @Override
    public void add(Iterable<Preference> items) {

    }

    @Override
    public void update(Preference item) {

    }

    @Override
    public void remove(Preference item) {

    }

    @Override
    public Observable<List<Preference>> query(Specification specification) {
        return null;
    }
}
