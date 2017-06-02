package pl.rmakowiecki.eventhub.ui.screen_event_calendar;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import java.util.List;
import pl.rmakowiecki.eventhub.R;
import pl.rmakowiecki.eventhub.model.local.Event;
import pl.rmakowiecki.eventhub.model.local.EventWDistance;
import pl.rmakowiecki.eventhub.ui.BaseFragment;
import pl.rmakowiecki.eventhub.util.SortTypes;

public class EventsFragment extends BaseFragment<EventsFragmentPresenter> implements EventsFragmentView {

    public static final String ARG_PAGE = "ARG_PAGE";

    private int columnCount = 1;
    private OnListFragmentInteractionListener listener;
    private RecyclerView.Adapter adapter;
    private View view;
    private RecyclerView recyclerView;
    private int page;

    public EventsFragment() {
        //no-op
    }

    public static EventsFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        EventsFragment fragment = new EventsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt(ARG_PAGE);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_events_list, container, false);

        return view;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_events_list;
    }

    @Override
    protected void initPresenter() {
        presenter = new EventsFragmentPresenter(page);
    }

    @Override
    public void showEvents(List<EventWDistance> ewd) {
        initEvents(ewd);
    }

    @Override
    public void initEvents(List<EventWDistance> ewd) {
        Context context = getContext();
        adapter = new EventsAdapter(context, ewd, listener);
        recyclerView = (RecyclerView) view;
        recyclerView.setLayoutManager(new GridLayoutManager(context, columnCount));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sorting_menu_item_sooner:
                Toast toast = Toast.makeText(getContext(), "Data", Toast.LENGTH_SHORT);
                toast.show();
                return true;
            case R.id.sorting_menu_item_closer:
                Toast toast2 = Toast.makeText(getContext(), "Odległość", Toast.LENGTH_SHORT);
                toast2.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            listener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public void sortEvents(SortTypes type) {
        presenter.onSortRequested(type);
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Event event);
    }
}
