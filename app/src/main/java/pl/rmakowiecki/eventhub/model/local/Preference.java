package pl.rmakowiecki.eventhub.model.local;

import pl.rmakowiecki.eventhub.repository.DataItem;

public final class Preference implements DataItem {
    private final int id;
    private final String name;

    public Preference(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
