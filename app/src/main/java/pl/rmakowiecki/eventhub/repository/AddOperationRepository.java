package pl.rmakowiecki.eventhub.repository;

import rx.Observable;

public interface AddOperationRepository<D, S extends QueryStatus> {
    Observable<S> add(D item);

    Observable<S> add(Iterable<D> items);
}
