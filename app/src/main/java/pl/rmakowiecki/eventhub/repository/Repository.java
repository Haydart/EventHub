package pl.rmakowiecki.eventhub.repository;

import java.util.List;
import rx.Observable;

public interface Repository<T> {
    void add(T item);

    void add(Iterable<T> items);

    void update(T item);

    void remove(T item);

    Observable<List<T>> query(Specification specification);

    Observable<T> querySingle(Specification specification);
}
