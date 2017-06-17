package pl.rmakowiecki.eventhub.repository;

import rx.Observable;

public interface UpdateOperationRepository<D, S> {
    Observable<S> update(D item);
}
