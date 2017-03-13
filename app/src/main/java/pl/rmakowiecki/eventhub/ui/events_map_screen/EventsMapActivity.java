package pl.rmakowiecki.eventhub.ui.events_map_screen;

import android.os.Bundle;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import java.util.List;
import pl.rmakowiecki.eventhub.R;
import pl.rmakowiecki.eventhub.model.local.Event;
import pl.rmakowiecki.eventhub.ui.BaseActivity;

public class EventsMapActivity extends BaseActivity<EventsMapPresenter> implements EventsMapView, OnMapReadyCallback {

    private GoogleMap googleMap;
    private MapFragment googleMapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.onViewInitialization();
    }

    @Override
    public void initMap() {
        googleMapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.events_map_fragment);
        googleMapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        presenter.onMapViewInitialized();
    }

    @Override
    protected void initPresenter() {
        presenter = new EventsMapPresenter();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_events_map;
    }

    @Override
    public void showEvents(List<Event> eventsList) {
        // TODO: 12/03/2017
    }
}
