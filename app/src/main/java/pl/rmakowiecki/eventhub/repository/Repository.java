package pl.rmakowiecki.eventhub.repository;

public interface Repository<T> {
    void add(T item);

    void add(Iterable<T> items);

    void update(T item);

    void remove(T item);
}
