package pl.rmakowiecki.eventhub.repository;

import java.util.List;

import rx.Observable;

public interface QueryList<T> {
    Observable<List<T>> query(Specification specification);
}
