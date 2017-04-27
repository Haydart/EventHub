package pl.rmakowiecki.eventhub.background;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import pl.rmakowiecki.eventhub.R;
import pl.rmakowiecki.eventhub.model.local.LocationCoordinates;

public class FetchAddressIntentService extends IntentService {
    private LocationCoordinates location;
    private ResultReceiver receiver;

    public FetchAddressIntentService() {
        super(FetchAddressIntentService.class.getSimpleName());
    }

    public FetchAddressIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        String errorMessage = "";
        location = intent.getParcelableExtra(Constants.LOCATION_DATA_EXTRA);
        receiver = intent.getParcelableExtra(Constants.RECEIVER);
        List<Address> addressList = null;

        try {
            addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        } catch (IOException ioException) {
            errorMessage = getApplicationContext().getString(R.string.reverse_geocoding_service_unavailable);
        } catch (IllegalArgumentException illegalArgumentException) {
            errorMessage = "Invalid location";
        }

        if (addressList == null || addressList.size() == 0) {
            if (errorMessage.isEmpty()) {
                errorMessage = getApplicationContext().getString(R.string.reverse_geocoding_no_address_found_error);
            }
            deliverResultToReceiver(Constants.FAILURE_RESULT, errorMessage);
        } else {
            Address address = addressList.get(0);
            deliverResultToReceiver(Constants.SUCCESS_RESULT, address.getAddressLine(0));
        }
    }

    private void deliverResultToReceiver(int resultCode, String message) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.RESULT_DATA_KEY, message);
        receiver.send(resultCode, bundle);
    }
}
