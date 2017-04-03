package pl.rmakowiecki.eventhub.api;

import android.support.annotation.NonNull;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscription;

public abstract class BaseApiInteractor<I extends ResponseInterceptor> extends RemoteServiceInteractor<I> {
    protected Retrofit retrofit;
    protected List<Subscription> apiCallsSubscriptions;

    protected BaseApiInteractor(String baseUrl) {
        retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(getGsonConverterFactory())
                .baseUrl(baseUrl)
                .build();

        apiCallsSubscriptions = new ArrayList<>();
    }

    @NonNull
    protected GsonConverterFactory getGsonConverterFactory() {
        return GsonConverterFactory.create(
                new GsonBuilder()
                        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                        .create());
    }

    public void cancelSubscriptions() {
        for (Subscription subscription : apiCallsSubscriptions) {
            subscription.unsubscribe();
        }
    }
}
