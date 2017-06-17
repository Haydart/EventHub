package pl.rmakowiecki.eventhub.repository;

import rx.Observable;

public interface RemoveOperationRepository<D, S extends QueryStatus> {
    Observable<S> remove(D item);
}
