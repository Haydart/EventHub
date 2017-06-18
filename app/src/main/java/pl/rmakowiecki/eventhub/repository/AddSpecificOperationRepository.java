package pl.rmakowiecki.eventhub.repository;

import rx.Observable;

public interface AddSpecificOperationRepository<D, S extends QueryStatus> {
    Observable<S> add(String key, D item);

    Observable<S> add(String key, Iterable<D> items);
}