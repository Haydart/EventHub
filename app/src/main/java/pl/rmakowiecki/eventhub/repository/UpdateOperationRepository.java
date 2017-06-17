package pl.rmakowiecki.eventhub.repository;

import rx.Observable;

public interface UpdateOperationRepository<D, S extends QueryStatus> {
    Observable<S> update(D item);
}
