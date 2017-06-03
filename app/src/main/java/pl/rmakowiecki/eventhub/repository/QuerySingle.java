package pl.rmakowiecki.eventhub.repository;

import rx.Observable;

public interface QuerySingle<T> {
    Observable<T> querySingle(Specification specification);
}
