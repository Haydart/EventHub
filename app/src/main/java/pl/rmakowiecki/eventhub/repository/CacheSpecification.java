package pl.rmakowiecki.eventhub.repository;

public interface CacheSpecification<T> extends Specification {
    void accept(T item);
}
