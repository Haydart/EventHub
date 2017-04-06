package pl.rmakowiecki.eventhub;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import pl.rmakowiecki.eventhub.model.local.LocationCoordinates;
import rx.Observable;
import rx.subjects.PublishSubject;

@SuppressWarnings("MissingPermission")
public class RxLocationProvider implements LocationProvider, GoogleApiClient.OnConnectionFailedListener, ResultCallback<LocationSettingsResult> {
    private static final int LOCATION_UPDATES_INTERVAL = 10000;
    private static final int LOCATION_UPDATES_FASTEST_INTERVAL = 5000;

    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    @NonNull private LocationListener locationListener;
    private PublishSubject<LocationCoordinates> locationPublishSubject;
    private PendingResult<LocationSettingsResult> result;
    private Status status;

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
    public Observable<Status> isLocationTurnedOn() {
        return Observable.just(status);
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
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        result = LocationServices.SettingsApi.checkLocationSettings(
                googleApiClient,
                builder.build()
        );
        result.setResultCallback(this);
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

    @Override
    public void onResult(@NonNull LocationSettingsResult result) {
        status = result.getStatus();
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

