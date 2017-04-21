package pl.rmakowiecki.eventhub.ui.calendar_screen;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import pl.rmakowiecki.eventhub.R;
import pl.rmakowiecki.eventhub.model.local.Event;
import pl.rmakowiecki.eventhub.ui.calendar_screen.EventsFragment.OnListFragmentInteractionListener;

class EventsAdapter extends RecyclerView.Adapter<EventsViewHolder> {

    private OnListFragmentInteractionListener listener;
    private List<Event> items;

    public EventsAdapter(Context baseContext, List<Event> events, OnListFragmentInteractionListener listener) {
        final LayoutInflater layoutInflater = LayoutInflater.from(baseContext);
        final Context context = baseContext;
        items = events;
        this.listener = listener;
    }

    @Override
    public EventsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_events, parent, false);
        return new EventsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final EventsViewHolder holder, int position) {
        holder.bindView(items.get(position));
        //TODO: Optimize logic behind list fields content, it's nothing like it should be

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
