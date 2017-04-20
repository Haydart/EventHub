package pl.rmakowiecki.eventhub.ui.events_map_screen;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.OnClick;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tbruyelle.rxpermissions.RxPermissions;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import pl.rmakowiecki.eventhub.R;
import pl.rmakowiecki.eventhub.model.local.LocationCoordinates;
import pl.rmakowiecki.eventhub.model.local.Place;
import pl.rmakowiecki.eventhub.ui.BaseActivity;
import pl.rmakowiecki.eventhub.ui.preferences_screen.PreferenceActivity;
import pl.rmakowiecki.eventhub.ui.preferences_screen.PreferenceCategory;
import pl.rmakowiecki.eventhub.util.ViewAnimationListenerAdapter;
import rx.android.schedulers.AndroidSchedulers;

public class EventsMapActivity extends BaseActivity<EventsMapPresenter> implements EventsMapView,
        OnMapReadyCallback,
        GoogleMap.OnCameraIdleListener,
        GoogleMap.OnCameraMoveListener,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerClickListener,
        NavigationView.OnNavigationItemSelectedListener {

    private static final float DEFAULT_MAP_ZOOM = 17f;
    private static final float MIN_MAP_ZOOM = 9f;
    private static final long PERMISSION_CHECKING_DELAY = 250;
    private static final String PREFERENCE_CATEGORY_PARCEL_KEY = "preference_category";
    public static final int FAB_ANIMATION_DURATION = 300;
    public static final int FAB_FULL_SCALE = 1;

    @BindView(R.id.drawer_layout) DrawerLayout drawer;
    @BindView(R.id.nav_view) NavigationView navigationView;
    @BindView(R.id.bottom_sheet_fab) FloatingActionButton bottomSheetFab;
    @BindView(R.id.map_bottom_sheet) LinearLayout mapBottomSheet;
    @BindView(R.id.place_name_text_view) TextView placeNameTextView;
    @BindView(R.id.place_address_text_view) TextView placeAddressTextView;
    @BindView(R.id.map_search_bar) FrameLayout mapSearchBar;
    @BindView(R.id.place_autocomplete_search_button) ImageButton placeAutocompleteSearchButton;
    @BindView(R.id.place_autocomplete_search_input) EditText placeAutocompleteEditText;

    private GoogleMap googleMap;
    private BottomSheetBehavior bottomSheetBehavior;
    private Marker mapClickMarker;
    private List<PreferenceCategory> preferenceCategories;

    @OnClick(R.id.navigation_drawer_icon)
    public void onMenuIconClicked() {
        drawer.openDrawer(GravityCompat.START, true);
    }

    @OnClick(R.id.search_button)
    public void onSearchButtonClicked() {
        placeAutocompleteEditText.performClick();
    }

    @OnClick(R.id.bottom_sheet_fab)
    public void onBottomSheetFabClicked() {
        // TODO: 23/03/2017 remove debug
        Toast.makeText(this, "Fab click", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.map_bottom_sheet)
    public void onBottomSheetClicked() {
        // TODO: 23/03/2017 remove debug
        Toast.makeText(this, "Bottom sheet click", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkLocationPermissions();
        initMapBottomSheet();
        initNavigationDrawer();
        initSearchBar();
        initPreferences();
    }

    private void initPreferences() {
        preferenceCategories = getIntent().getParcelableArrayListExtra(PREFERENCE_CATEGORY_PARCEL_KEY);
    }

    private void checkLocationPermissions() {
        new RxPermissions(this)
                .request(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .delay(PERMISSION_CHECKING_DELAY, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(granted -> {
                    if (granted) {
                        presenter.onLocationPermissionsGranted();
                    } else {
                        // TODO: 14/03/2017 implement user revoke reaction
                    }
                });
    }

    private void initMapBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(mapBottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (BottomSheetBehavior.STATE_DRAGGING == newState) {
                    bottomSheetFab.animate()
                            .scaleX(0)
                            .scaleY(0)
                            .setDuration(FAB_ANIMATION_DURATION)
                            .start();
                } else if (BottomSheetBehavior.STATE_COLLAPSED == newState) {
                    bottomSheetFab.animate()
                            .scaleX(FAB_FULL_SCALE)
                            .scaleY(FAB_FULL_SCALE)
                            .setDuration(FAB_ANIMATION_DURATION)
                            .start();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                //no-op
            }
        });
    }

    private void initNavigationDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initSearchBar() {
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        placeAutocompleteSearchButton.setVisibility(View.GONE);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(com.google.android.gms.location.places.Place place) {
                presenter.onSearchedPlaceSelected(new Place(
                        place.getId(),
                        place.getName().toString(),
                        place.getAddress().toString(),
                        new LocationCoordinates(
                                place.getLatLng().latitude,
                                place.getLatLng().longitude
                        )
                ));
            }

            @Override
            public void onError(Status status) {
                presenter.onPlaceSearchError();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.onViewInitialization();
    }

    @Override
    public void showLocationSettingsDialog(StatusWrapper status){
        try {
            status.getStatus().startResolutionForResult(this, 0);
        } catch (IntentSender.SendIntentException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void setMapPadding(int left, int top, int right, int bottom) {
        googleMap.setPadding(left, top, right, bottom);
    }

    @Override
    public void setBottomSheetData(String name, String address) {
        placeNameTextView.setText(name);
        placeAddressTextView.setText(address);
    }

    @Override
    public void initMap() {
        final MapFragment googleMapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.events_map_fragment);
        googleMapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        applyMapSettings();
        presenter.onMapInitialized();
    }

    @SuppressWarnings("MissingPermission")
    private void applyMapSettings() {
        googleMap.setMinZoomPreference(MIN_MAP_ZOOM);
        googleMap.setOnCameraIdleListener(this);
        googleMap.setOnMapClickListener(this);
        googleMap.setOnMarkerClickListener(this);
        googleMap.setOnCameraMoveListener(this);
        UiSettings uiSettings = googleMap.getUiSettings();
        uiSettings.setMyLocationButtonEnabled(false);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        presenter.onMapClicked(new LocationCoordinates(latLng.latitude, latLng.longitude));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        LatLng markerPosition = marker.getPosition();
        presenter.onMapMarkerClicked(
                new LocationCoordinates(markerPosition.latitude, markerPosition.longitude)
        );
        return false;
    }

    @Override
    public void onCameraIdle() {
        presenter.onMapCameraIdle();
    }

    @Override
    public void onCameraMove() {
        presenter.onMapCameraMove();
    }

    @Override
    public void moveMapCamera(LocationCoordinates location) {
        LatLng cameraTarget = new LatLng(location.getLatitude(), location.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(cameraTarget, DEFAULT_MAP_ZOOM);
        googleMap.animateCamera(cameraUpdate);
    }

    @Override
    public void showMapMarker(LocationCoordinates locationCoordinates) {
        mapClickMarker = googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(locationCoordinates.getLatitude(), locationCoordinates.getLongitude()))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
        );
    }

    @Override
    public void hideMapMarker() {
        mapClickMarker.setVisible(false);
    }

    @Override
    public void showBottomSheet() {
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            bottomSheetFab.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideBottomSheet() {
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            bottomSheetFab.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    @SuppressWarnings("MissingPermission")
    public void showDeviceLocation() {
        googleMap.setMyLocationEnabled(true);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_navigation;
    }

    @Override
    public void showPlaces(List<Place> placesList) {
        for (Place place : placesList) {
            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(
                            place.getLocationCoordinates().getLatitude(),
                            place.getLocationCoordinates().getLongitude())
                    )
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                    .title(place.getName())
            );
        }
    }

    @Override
    public void hideSearchBar(boolean animate) {
        Animation hideAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        hideAnimation.setAnimationListener(new ViewAnimationListenerAdapter() {
            @Override
            public void onAnimationEnd(Animation animation) {
                mapSearchBar.setVisibility(View.INVISIBLE);
            }
        });
        mapSearchBar.startAnimation(hideAnimation);
    }

    @Override
    public void showSearchBar(boolean animate) {
        mapSearchBar.setVisibility(View.VISIBLE);
        Animation showAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_down);
        mapSearchBar.startAnimation(showAnimation);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.overflow_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_camera:
                Toast.makeText(getApplicationContext(), "First sample item clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_share:
                Toast.makeText(getApplicationContext(), "Second sample item clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_preferences:
                Intent intent = new Intent(this, PreferenceActivity.class);
                intent.putParcelableArrayListExtra(PREFERENCE_CATEGORY_PARCEL_KEY, (ArrayList<? extends Parcelable>) preferenceCategories);
                startActivity(intent);
                finish();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void initPresenter() {
        presenter = new EventsMapPresenter();
    }
}
