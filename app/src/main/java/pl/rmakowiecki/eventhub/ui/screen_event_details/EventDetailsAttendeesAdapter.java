package pl.rmakowiecki.eventhub.ui.screen_event_details;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import pl.rmakowiecki.eventhub.R;
import pl.rmakowiecki.eventhub.model.local.UserProfile;

public class EventDetailsAttendeesAdapter extends RecyclerView.Adapter<EventDetailsAttendeesViewHolder> {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<UserProfile> attendees;

    public EventDetailsAttendeesAdapter(Context appContext, List<UserProfile> attendees) {
        context = appContext;
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
        UserProfile attendee = attendees.get(position);
        holder.bindView("DOMINIK KOZŁOWSKI", attendee.getPictureData());
    }

    @Override
    public int getItemCount() {
        return attendees.size();
    }
}
