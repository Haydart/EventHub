package pl.rmakowiecki.eventhub.ui.screen_event_calendar;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import java.util.List;
import pl.rmakowiecki.eventhub.R;
import pl.rmakowiecki.eventhub.model.local.EventWDistance;
import pl.rmakowiecki.eventhub.ui.screen_event_calendar.EventsFragment.OnListFragmentInteractionListener;

class EventsAdapter extends RecyclerView.Adapter<EventsViewHolder> {

    @BindView(R.id.loading_panel) RelativeLayout progressBar;

    private OnListFragmentInteractionListener listener;
    private List<EventWDistance> items;
    private EventsFragmentPresenter presenter;

    public EventsAdapter(Context baseContext, List<EventWDistance> events, OnListFragmentInteractionListener listener, EventsFragmentPresenter presenter) {
        final LayoutInflater layoutInflater = LayoutInflater.from(baseContext);
        final Context context = baseContext;
        items = events;
        this.listener = listener;
        this.presenter = presenter;
    }

    @Override
    public EventsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_events, parent, false);
        ButterKnife.bind(this, view);
        return new EventsViewHolder(view, presenter);
    }

    @Override
    public void onBindViewHolder(final EventsViewHolder holder, int position) {
        holder.bindView(items.get(position));
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
