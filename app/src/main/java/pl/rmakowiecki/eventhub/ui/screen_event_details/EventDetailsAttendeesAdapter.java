package pl.rmakowiecki.eventhub.ui.screen_event_details;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.rmakowiecki.eventhub.R;
import pl.rmakowiecki.eventhub.model.local.EventAttendee;
import pl.rmakowiecki.eventhub.ui.screen_user_profile.UserProfileActivity;
import pl.rmakowiecki.eventhub.ui.screen_user_profile.UserProfileImageSpecification;
import pl.rmakowiecki.eventhub.ui.screen_user_profile.UserProfileImageRepository;
import pl.rmakowiecki.eventhub.util.BitmapUtils;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static pl.rmakowiecki.eventhub.background.Constants.USER_PROFILE_EXTRA_IS_DIFFERENT_USER;
import static pl.rmakowiecki.eventhub.background.Constants.USER_PROFILE_EXTRA_USER_ID;

public class EventDetailsAttendeesAdapter extends RecyclerView.Adapter<EventDetailsAttendeesViewHolder> {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<EventAttendee> attendees;
    private Map<String, byte[]> userPictureMap;

    public EventDetailsAttendeesAdapter(Context appContext, List<EventAttendee> attendees) {
        context = appContext;
        userPictureMap = new HashMap<>();
        layoutInflater = LayoutInflater.from(context);
        this.attendees = attendees;
    }

    @Override
    public EventDetailsAttendeesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.event_details_attending_user_layout, parent, false);
        return new EventDetailsAttendeesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EventDetailsAttendeesViewHolder holder, int position) {
        EventAttendee attendee = attendees.get(position);
        holder.bindView(attendee.getName());
        holder.itemView.setOnClickListener(v -> launchUserProfileActivity(attendee.getId()));

        if (userPictureMap.containsKey(attendee.getId())) {
            holder.loadPicture(BitmapUtils.getBitmapFromBytes(userPictureMap.get(attendee.getId())));
        } else {
            UserProfileImageSpecification specification = new UserProfileImageSpecification(attendee.getId());
            new UserProfileImageRepository()
                    .querySingle(specification)
                    .compose(applySchedulers())
                    .subscribe(pictureData -> {
                        if (pictureData != null) {
                            userPictureMap.put(attendee.getId(), pictureData);

                            if (holder != null) {
                                holder.loadPicture(BitmapUtils.getBitmapFromBytes(pictureData));
                            }
                        }
                    });
        }
    }

    private void launchUserProfileActivity(String attendeeId) {
        Intent intent = new Intent(context, UserProfileActivity.class);
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(USER_PROFILE_EXTRA_IS_DIFFERENT_USER, true);
        intent.putExtra(USER_PROFILE_EXTRA_USER_ID, attendeeId);
        context.startActivity(intent);
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
