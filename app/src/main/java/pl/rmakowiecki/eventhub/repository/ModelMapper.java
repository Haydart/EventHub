package pl.rmakowiecki.eventhub.repository;

public interface ModelMapper<From, To> {
    To map(From model);
}
