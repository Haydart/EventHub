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
import pl.rmakowiecki.eventhub.ui.BaseFragment;

/**
 * A fragment representing a list of Items.
 * <p />
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class EventsFragment extends BaseFragment<EventsFragmentPresenter> implements EventsFragmentView {

    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private RecyclerView.Adapter adapter;
    View view;

    public static final String ARG_PAGE = "ARG_PAGE";

    public EventsFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
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
        final int mPage = getArguments().getInt(ARG_PAGE);
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
        Context context = view.getContext();
        RecyclerView recyclerView = (RecyclerView) view;
        adapter = new EventsAdapter(getActivity().getBaseContext(), events, mListener);
        recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Event event);
    }
}
