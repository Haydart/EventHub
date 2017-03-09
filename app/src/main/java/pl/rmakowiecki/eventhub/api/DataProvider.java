package pl.rmakowiecki.eventhub.api;

import java.util.List;

public interface DataProvider<T> {
    T fetch();
    List<T> fetchList();
}
