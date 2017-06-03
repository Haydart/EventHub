package pl.rmakowiecki.eventhub.ui.screen_event_calendar;

import java.util.Comparator;
import pl.rmakowiecki.eventhub.model.local.Event;
import pl.rmakowiecki.eventhub.model.local.EventWDistance;

/**
 * Created by m1per on 31.05.2017.
 */

enum EventComparator implements Comparator<EventWDistance> {
    DATE_SORT {
        public int compare(EventWDistance e1, EventWDistance e2) {
            return Long.valueOf(e1.getEvent().getTimestamp()).compareTo(e2.getEvent().getTimestamp());
        }
    },
    DISTANCE_SORT {
        public int compare(EventWDistance e1, EventWDistance e2) {
            return Double.valueOf(e1.getDistanceCalculable()).compareTo(e2.getDistanceCalculable());
        }
    };

    public static Comparator<EventWDistance> descending(final Comparator<EventWDistance> other) {
        return new Comparator<EventWDistance>() {
            public int compare(EventWDistance e1, EventWDistance e2) {
                return -1 * other.compare(e1, e2);
            }
        };
    }

    public static Comparator<EventWDistance> ascending(final Comparator<EventWDistance> other) {
        return new Comparator<EventWDistance>() {
            public int compare(EventWDistance e1, EventWDistance e2) {
                return other.compare(e1, e2);
            }
        };
    }

    public static Comparator<EventWDistance> getComparator(final EventComparator... multipleOptions) {
        return new Comparator<EventWDistance>() {
            public int compare(EventWDistance e1, EventWDistance e2) {
                for (EventComparator option : multipleOptions) {
                    int result = option.compare(e1, e2);
                    if (result != 0) {
                        return result;
                    }
                }
                return 0;
            }
        };
    }
}
