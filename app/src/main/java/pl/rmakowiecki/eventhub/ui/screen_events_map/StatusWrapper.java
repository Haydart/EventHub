package pl.rmakowiecki.eventhub.ui.screen_events_map;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationSettingsStatusCodes;

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

    public boolean isLocationSettingRequired() {
        return status.getStatus().getStatusCode() == LocationSettingsStatusCodes.RESOLUTION_REQUIRED;
    }
}
