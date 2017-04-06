package pl.rmakowiecki.eventhub.ui.events_map_screen;

import android.content.IntentSender;
import com.google.android.gms.common.api.Status;
import com.jenzz.noop.annotation.NoOp;
import java.util.List;
import pl.rmakowiecki.eventhub.model.local.LocationCoordinates;
import pl.rmakowiecki.eventhub.model.local.Place;
import pl.rmakowiecki.eventhub.ui.BaseView;

@NoOp
interface EventsMapView extends BaseView {
    void showPlaces(List<Place> placesList);

    void initMap();

    void moveMapCamera(LocationCoordinates locationCoordinates);

    void showMapClickMarker(LocationCoordinates locationCoordinates);

    void hideMapClickMarker();

    void showBottomSheet();

    void showDeviceLocation();

    void hideBottomSheet();

    void setBottomMapPadding();

    void setDefaultMapPadding();

    void showLocationSettingsDialog(Status status) throws IntentSender.SendIntentException;
}
