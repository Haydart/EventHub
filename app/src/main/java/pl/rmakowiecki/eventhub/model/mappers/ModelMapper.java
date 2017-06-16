package pl.rmakowiecki.eventhub.model.mappers;

public interface ModelMapper<From, To> {
    To map(From model);
}
