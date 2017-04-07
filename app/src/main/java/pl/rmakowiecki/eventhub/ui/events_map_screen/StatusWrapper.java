package pl.rmakowiecki.eventhub.ui.events_map_screen;

import com.google.android.gms.common.api.Status;

/**
 * Created by m1per on 07.04.2017.
 */

public class StatusWrapper {

    Status status;

    public StatusWrapper(Status status)
    {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }
}
