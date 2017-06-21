package pl.rmakowiecki.eventhub.ui.screen_user_profile;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.rmakowiecki.eventhub.R;
import pl.rmakowiecki.eventhub.model.local.Event;
import pl.rmakowiecki.eventhub.ui.screen_event_details.EventImageRepository;
import pl.rmakowiecki.eventhub.ui.screen_event_details.EventImageSpecification;
import pl.rmakowiecki.eventhub.util.BitmapUtils;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UserProfileEventsAdapter extends RecyclerView.Adapter<UserProfileEventsViewHolder> {

    private LayoutInflater layoutInflater;
    private List<Event> events;
    private Map<String, byte[]> eventImageMap;

    public UserProfileEventsAdapter(Context context, List<Event> events) {
        eventImageMap = new HashMap<>();
        layoutInflater = LayoutInflater.from(context);
        this.events = events;
    }

    @Override
    public UserProfileEventsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.user_profile_event_list_item, parent, false);
        return new UserProfileEventsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserProfileEventsViewHolder holder, int position) {
        Event event = events.get(position);
        holder.bindView(event);

        if (eventImageMap.containsKey(event.getId())) {
            holder.loadPicture(BitmapUtils.getBitmapFromBytes(eventImageMap.get(event.getId())));
        }
        else {
            EventImageSpecification specification = new EventImageSpecification(event.getId());
            new EventImageRepository()
                    .querySingle(specification)
                    .compose(applySchedulers())
                    .subscribe(pictureData -> {
                        if (pictureData != null) {
                            eventImageMap.put(event.getId(), pictureData);
                            if (holder != null)
                                holder.loadPicture(BitmapUtils.getBitmapFromBytes(pictureData));
                        }
                    });
        }
    }

    protected <T> Observable.Transformer<T, T> applySchedulers() {
        return observable -> observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public int getItemCount() {
        return events.size();
    }
}
