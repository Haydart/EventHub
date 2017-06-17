package pl.rmakowiecki.eventhub.repository;

import rx.Observable;

public interface Repository<A, B> {
    Observable<B> add(A item);

    Observable<B> add(Iterable<A> items);

    void update(A item);

    void remove(A item);
}
