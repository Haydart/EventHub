package pl.rmakowiecki.eventhub.ui.calendar_screen;

import java.io.Serializable;

/**
 * Created by m1per on 17.04.2017.
 */

public class Event implements Serializable {

    public Long timestamp;
    public String location;
    public String name;
    public String organizer;

    public Event() {
    }

    public Event(Long timestamp, String location, String name, String organizer) {
        this.timestamp = timestamp;
        this.location = location;
        this.name = name;
        this.organizer = organizer;
    }
}


