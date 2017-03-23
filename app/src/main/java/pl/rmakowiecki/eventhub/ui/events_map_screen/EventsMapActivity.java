package pl.rmakowiecki.eventhub.ui.events_map_screen;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.OnClick;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;
import com.tbruyelle.rxpermissions.RxPermissions;
import java.util.List;
import java.util.concurrent.TimeUnit;
import pl.rmakowiecki.eventhub.R;
import pl.rmakowiecki.eventhub.model.local.Event;
import pl.rmakowiecki.eventhub.model.local.LocationCoordinates;
import pl.rmakowiecki.eventhub.ui.BaseActivity;
import rx.android.schedulers.AndroidSchedulers;

public class EventsMapActivity extends BaseActivity<EventsMapPresenter> implements EventsMapView,
        OnMapReadyCallback,
        GoogleMap.OnCameraIdleListener,
        GoogleMap.OnCameraMoveListener,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnPoiClickListener,
        NavigationView.OnNavigationItemSelectedListener {

    private static final float DEFAULT_MAP_ZOOM = 17f;
    private static final float MIN_MAP_ZOOM = 9f;
    private static final long PERMISSION_GRANTING_DELAY = 250;
    public static final int MAP_PADDING_TOP = 64;
    private static final int BOTTOM_SHEET_MAP_PADDING = 256;

    @BindView(R.id.drawer_layout) DrawerLayout drawer;
    @BindView(R.id.nav_view) NavigationView navigationView;
    @BindView(R.id.bottom_sheet_fab) FloatingActionButton bottomSheetFab;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.map_bottom_sheet) FrameLayout bottomSheetLayout;

    private GoogleMap googleMap;
    private Marker mapClickMarker;
    private BottomSheetBehavior bottomSheetBehavior;

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
        setSupportActionBar(toolbar);
        checkLocationPermissions();
        initMapBottomSheet();
        initNavigationDrawer();
    }

    private void checkLocationPermissions() {
        new RxPermissions(this)
                .request(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .delay(PERMISSION_GRANTING_DELAY, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(granted -> {
                    if (granted) {
                        presenter.onLocationPermissionsGranted();
                    } else {
                        // TODO: 14/03/2017 implement user revoke reaction
                    }
                });
    }

    private void initNavigationDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initMapBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (BottomSheetBehavior.STATE_DRAGGING == newState) {
                    bottomSheetFab.animate().scaleX(0).scaleY(0).setDuration(300).start();
                } else if (BottomSheetBehavior.STATE_COLLAPSED == newState) {
                    bottomSheetFab.animate().scaleX(1).scaleY(1).setDuration(300).start();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                //no-op
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.onViewInitialization();
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
        presenter.onMapViewInitialized();
    }

    @SuppressWarnings("MissingPermission")
    private void applyMapSettings() {
        googleMap.setPadding(0, MAP_PADDING_TOP, 0, 0);
        googleMap.setMinZoomPreference(MIN_MAP_ZOOM);
        googleMap.setOnCameraIdleListener(this);
        googleMap.setOnMapClickListener(this);
        googleMap.setOnCameraMoveListener(this);
        UiSettings uiSettings = googleMap.getUiSettings();
        uiSettings.setMyLocationButtonEnabled(false);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        presenter.onMapClicked(new LocationCoordinates(latLng.latitude, latLng.longitude));
    }

    @Override
    public void onPoiClick(PointOfInterest pointOfInterest) {
        Toast.makeText(this, "Point of interest click", Toast.LENGTH_SHORT).show();
        // TODO: 23/03/2017 remove debug
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
    public void showMapClickMarker(LocationCoordinates locationCoordinates) {
        mapClickMarker = googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(locationCoordinates.getLatitude(), locationCoordinates.getLongitude())));
    }

    @Override
    public void hideMapClickMarker(LocationCoordinates locationCoordinates) {
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
    public void setBottomMapPadding() {
        googleMap.setPadding(0, MAP_PADDING_TOP, 0, BOTTOM_SHEET_MAP_PADDING);
    }

    @Override
    public void setDefaultMapPadding() {
        googleMap.setPadding(0, MAP_PADDING_TOP, 0, 0);
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
    public void showEvents(List<Event> eventsList) {
        // TODO: 12/03/2017
    }

    @Override
    protected void initPresenter() {
        presenter = new EventsMapPresenter();
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

        if (id == R.id.nav_camera) {
            Toast.makeText(getApplicationContext(), "First sample item clicked", Toast.LENGTH_SHORT).show();
        }else if (id == R.id.nav_share) {
            Toast.makeText(getApplicationContext(), "Second sample item clicked", Toast.LENGTH_SHORT).show();

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
