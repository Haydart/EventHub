package pl.rmakowiecki.eventhub.ui.screen_event_calendar;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;
import pl.rmakowiecki.eventhub.R;
import pl.rmakowiecki.eventhub.model.local.EventWDistance;
import pl.rmakowiecki.eventhub.ui.screen_event_calendar.EventsFragment.OnListFragmentInteractionListener;

class EventsAdapter extends RecyclerView.Adapter<EventsViewHolder> {

    private OnListFragmentInteractionListener listener;
    private List<EventWDistance> items;
    private UnauthorizedEventJoinListener unauthorizedListener;
    private List<Boolean> attendance;
    private EventsFragmentPresenter presenter;

    public EventsAdapter(Context baseContext, List<EventWDistance> events, List<Boolean> attendance, EventsFragment.OnListFragmentInteractionListener listener,
            EventsFragmentPresenter presenter) {
        final Context context = baseContext;
        items = events;
        this.listener = listener;
        this.presenter = presenter;
        this.unauthorizedListener = (UnauthorizedEventJoinListener) context;
        this.attendance = attendance;
    }

    @Override
    public EventsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_events, parent, false);
        return new EventsViewHolder(view, presenter, unauthorizedListener);
    }

    @Override
    public void onBindViewHolder(final EventsViewHolder holder, int position) {
        holder.bindView(items.get(position), attendance.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
