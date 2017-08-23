package pl.rmakowiecki.eventhub.ui.screen_event_calendar;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;
import pl.rmakowiecki.eventhub.R;
import pl.rmakowiecki.eventhub.model.local.EventWDistance;

class MyEventsAdapter extends RecyclerView.Adapter<EventsViewHolder> {

    private MyEventsFragment.OnListFragmentInteractionListener listener;
    private UnauthorizedEventJoinListener unauthorizedListener;
    private List<EventWDistance> items;
    private MyEventsFragmentPresenter presenter;
    private List<Boolean> attendance;

    public MyEventsAdapter(Context baseContext, List<EventWDistance> events, List<Boolean> attendance,
            MyEventsFragment.OnListFragmentInteractionListener listener,
            MyEventsFragmentPresenter presenter) {
        final LayoutInflater layoutInflater = LayoutInflater.from(baseContext);
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
