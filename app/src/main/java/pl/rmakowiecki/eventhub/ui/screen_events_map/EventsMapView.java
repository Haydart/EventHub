package pl.rmakowiecki.eventhub.ui.screen_events_map;

import com.jenzz.noop.annotation.NoOp;
import java.util.List;
import pl.rmakowiecki.eventhub.model.local.LocationCoordinates;
import pl.rmakowiecki.eventhub.model.local.Place;
import pl.rmakowiecki.eventhub.ui.BaseView;

@NoOp
interface EventsMapView extends BaseView {
    void showPlaces(List<Place> placesList);

    void hideSearchBar(boolean animate);

    void showSearchBar(boolean animate);

    void initMap();

    void moveMapCamera(LocationCoordinates locationCoordinates);

    void showMapMarker(LocationCoordinates locationCoordinates);

    void hideMapMarker();

    void showBottomSheet();

    void showDeviceLocation();

    void hideBottomSheet();

    void showLocationSettingsDialog(StatusWrapper sw);

    void setMapPadding(int left, int top, int right, int bottom);

    void setBottomSheetData(String name, String address);

    void fetchAddressForLocation(LocationCoordinates location);

    void launchSignInScreen();

    void launchPreferencesScreen();

    void launchCalendarScreen();

    void launchUserProfileScreen();

    void updateNavigationDrawer(boolean loggedIn);
}
