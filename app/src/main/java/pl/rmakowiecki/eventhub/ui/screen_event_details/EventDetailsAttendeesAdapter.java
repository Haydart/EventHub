package pl.rmakowiecki.eventhub.ui.screen_event_details;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import pl.rmakowiecki.eventhub.R;
import pl.rmakowiecki.eventhub.model.local.EventAttendee;
import pl.rmakowiecki.eventhub.ui.screen_user_profile.UserProfileImageSpecification;
import pl.rmakowiecki.eventhub.ui.screen_user_profile.UserProfileImageRepository;
import pl.rmakowiecki.eventhub.util.BitmapUtils;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class EventDetailsAttendeesAdapter extends RecyclerView.Adapter<EventDetailsAttendeesViewHolder> {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<EventAttendee> attendees;
    private UserProfileImageRepository profileImageRepository;

    public EventDetailsAttendeesAdapter(Context appContext, List<EventAttendee> attendees) {
        context = appContext;
        layoutInflater = LayoutInflater.from(context);
        this.attendees = attendees;
        profileImageRepository = new UserProfileImageRepository();
    }

    @Override
    public EventDetailsAttendeesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.event_details_attending_user_layout, parent, false);
        return new EventDetailsAttendeesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EventDetailsAttendeesViewHolder holder, int position) {
        EventAttendee attendee = attendees.get(position);
        holder.bindView(attendee.getName(), attendee.getId());

        UserProfileImageSpecification specification = new UserProfileImageSpecification(attendee.getId());
        byte[] image = profileImageRepository.getImage(specification);
        if (image != null) {
            holder.loadPicture(BitmapUtils.getBitmapFromBytes(image));
        }
        else {
            profileImageRepository
                    .querySingle(specification)
                    .compose(applySchedulers())
                    .subscribe(pictureData -> {
                        if (pictureData != null) {
                            profileImageRepository.add(attendee.getId(), pictureData);

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
        return attendees.size();
    }
}
