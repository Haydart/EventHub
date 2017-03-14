package pl.rmakowiecki.eventhub;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import pl.rmakowiecki.eventhub.model.local.LocationCoordinates;
import rx.Observable;
import rx.subjects.PublishSubject;

@SuppressWarnings("MissingPermission")
public class RxLocationProvider implements LocationProvider, GoogleApiClient.OnConnectionFailedListener {
    private static final int LOCATION_UPDATES_INTERVAL = 1000;
    private static final int LOCATION_UPDATES_FASTEST_INTERVAL = 200;

    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    @NonNull private LocationListener locationListener;
    private PublishSubject<LocationCoordinates> locationPublishSubject;

    public RxLocationProvider() {
        initLocationRequest();
        locationListener = this::publishLocation;
        locationPublishSubject = PublishSubject.create();
    }

    private void initLocationRequest() {
        locationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(LOCATION_UPDATES_INTERVAL)
                .setFastestInterval(LOCATION_UPDATES_FASTEST_INTERVAL);
    }

    @Override
    public Observable<LocationCoordinates> getLocation() {
        buildAndConnectApiClient(() -> publishLocation(LocationServices.FusedLocationApi.getLastLocation(googleApiClient)));
        return locationPublishSubject;
    }

    private void publishLocation(Location location) {
        locationPublishSubject.onNext(mapToLocalModel(location));
    }

    @Override
    public Observable<LocationCoordinates> getLocationUpdates() {
        buildAndConnectApiClient(() -> LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, locationListener));
        return locationPublishSubject;
    }

    @Override
    public void stopLocationTracking() {
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, locationListener);
        googleApiClient.disconnect();
    }

    private void buildAndConnectApiClient(OnClientConnectedListener connectionListener) {
        googleApiClient = new GoogleApiClient.Builder(EventHubApplication.getContext())
                .addConnectionCallbacks(new ConnectionCallbacksAdapter(connectionListener))
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    private LocationCoordinates mapToLocalModel(Location location) {
        return location == null ? null : new LocationCoordinates(location.getLatitude(), location.getLongitude());
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        locationPublishSubject.onError(new Exception(connectionResult.getErrorMessage()));
    }

    private interface OnClientConnectedListener {
        void onConnected();
    }

    private static class ConnectionCallbacksAdapter implements GoogleApiClient.ConnectionCallbacks {
        private OnClientConnectedListener connectionListener;

        private ConnectionCallbacksAdapter(OnClientConnectedListener connectionListener) {
            this.connectionListener = connectionListener;
        }

        @Override
        public void onConnected(@Nullable Bundle bundle) {
            connectionListener.onConnected();
        }

        @Override
        public void onConnectionSuspended(int reason) {
            //no-op
        }
    }
}

