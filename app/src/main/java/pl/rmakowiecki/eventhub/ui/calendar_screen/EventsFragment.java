package pl.rmakowiecki.eventhub.ui.calendar_screen;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;
import pl.rmakowiecki.eventhub.R;
import pl.rmakowiecki.eventhub.model.local.Event;
import pl.rmakowiecki.eventhub.ui.BaseFragment;

public class EventsFragment extends BaseFragment<EventsFragmentPresenter> implements EventsFragmentView {

    public static final String ARG_PAGE = "ARG_PAGE";

    private int columnCount = 1;
    private OnListFragmentInteractionListener listener;
    private RecyclerView.Adapter adapter;
    private View view;
    private RecyclerView recyclerView;

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
        final int page = getArguments().getInt(ARG_PAGE);
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
        presenter = new EventsFragmentPresenter();
    }

    @Override
    public void showEvents(List<Event> events) {
        initEvents(events);
    }

    @Override
    public void initEvents(final List<Event> events) {
        Context context = getContext();
        adapter = new EventsAdapter(context, events, listener);
        recyclerView = (RecyclerView) view;
        recyclerView.setLayoutManager(new GridLayoutManager(context, columnCount));
        recyclerView.setAdapter(adapter);
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

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Event event);
    }
}