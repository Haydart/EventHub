package pl.rmakowiecki.eventhub.background;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

public class AddressResultReceiver extends ResultReceiver {
    private String addressOutput;
    private AddressGeocodingListener listener;

    public AddressResultReceiver(AddressGeocodingListener listener, Handler handler) {
        super(handler);
        this.listener = listener;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        addressOutput = resultData.getString(Constants.RESULT_DATA_KEY);
        listener.onLocationAddressFetched(addressOutput);
    }

    public interface AddressGeocodingListener {
        void onLocationAddressFetched(String addressOutput);
    }
}
