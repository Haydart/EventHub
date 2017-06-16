package pl.rmakowiecki.eventhub.ui.screen_event_details;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;
import pl.rmakowiecki.eventhub.R;
import pl.rmakowiecki.eventhub.model.local.EventAttendee;
import pl.rmakowiecki.eventhub.model.local.User;

public class EventDetailsAttendeesAdapter extends RecyclerView.Adapter<EventDetailsAttendeesViewHolder> {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<EventAttendee> attendees;

    public EventDetailsAttendeesAdapter(Context appContext, List<EventAttendee> attendees) {
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
        EventAttendee attendee = attendees.get(position);
        holder.bindView(attendee.getName(), new byte[] { 2, 1, 3, 7});
    }

    @Override
    public int getItemCount() {
        return attendees.size();
    }
}
