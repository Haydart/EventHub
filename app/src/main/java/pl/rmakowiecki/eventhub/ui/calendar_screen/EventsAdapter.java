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
import pl.rmakowiecki.eventhub.ui.calendar_screen.EventsFragment.OnListFragmentInteractionListener;

class EventsAdapter extends RecyclerView.Adapter<EventsViewHolder> {

    private OnListFragmentInteractionListener mListener = null;
    private List<Event> items;

    public EventsAdapter(Context baseContext, List<Event> events, OnListFragmentInteractionListener mListener) {
        final LayoutInflater layoutInflater = LayoutInflater.from(baseContext);
        final Context context = baseContext;
        items = events;
        this.mListener = mListener;
    }

    @Override
    public EventsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_events, parent, false);
        return new EventsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final EventsViewHolder holder, int position) {
        //TODO: Optimize logic behind list fields content, it's nothing like it should be
        Date d = new Date(items.get(position).timestamp * 1000);
        String days[] = { "PON.", "WT.", "ŚR.", "CZW.", "PT.", "SOB.", "ND." };

        String date = new SimpleDateFormat("dd-MM-yyyy").format(d);
        String day = new SimpleDateFormat("dd").format(d);
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        String dayName = days[dayOfWeek - 1];

        holder.mItem = items.get(position);
        holder.nameTextView.setText(items.get(position).name);
        holder.organizerTextView.setText(items.get(position).organizer);
        holder.dateTextView.setText(date);
        holder.dayDateTextView.setText(day);
        holder.dayNameTextView.setText(dayName);
        holder.locationTextView.setText(items.get(position).location);

        holder.mView.setOnClickListener(v -> {
            if (null != mListener) {
                mListener.onListFragmentInteraction(holder.mItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
