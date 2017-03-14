package pl.rmakowiecki.eventhub;

import android.app.Application;
import android.content.Context;

public class EventHubApplication extends Application {
    private static Context applicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = getApplicationContext();
    }

    public static Context getContext() {
        return applicationContext;
    }
}
