package pl.rmakowiecki.eventhub.ui.screen_event_calendar;

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
import pl.rmakowiecki.eventhub.model.local.EventWDistance;
import pl.rmakowiecki.eventhub.repository.GenericQueryStatus;
import pl.rmakowiecki.eventhub.ui.BaseFragment;
import pl.rmakowiecki.eventhub.util.PreferencesManager;
import pl.rmakowiecki.eventhub.util.SortTypes;

/**
 * Created by m1per on 17.06.2017.
 */

public class PersonalizedEventsFragment extends BaseFragment<PersonalizedEventsFragmentPresenter> implements PersonalizedEventsFragmentView {

    public static final String ARG_PAGE = "ARG_PAGE";
    private static final int PAGE = 0;
    private PreferencesManager preferencesManager;
    private int columnCount = 1;
    private PersonalizedEventsFragment.OnListFragmentInteractionListener listener;
    private RecyclerView.Adapter adapter;
    private View view;
    private RecyclerView recyclerView;

    public PersonalizedEventsFragment() {
        //no-op
    }

    public static PersonalizedEventsFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        PersonalizedEventsFragment fragment = new PersonalizedEventsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_personalized_events_list, container, false);

        return view;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_personalized_events_list;
    }

    @Override
    protected void initPresenter() {
        preferencesManager = new PreferencesManager(getContext());
        presenter = new PersonalizedEventsFragmentPresenter(PAGE, preferencesManager);
    }

    @Override
    public void showEvents(List<EventWDistance> eventWithDistance) {
        initEvents(eventWithDistance);
    }

    @Override
    public void initEvents(List<EventWDistance> eventWithDistance) {
        Context context = getContext();
        adapter = new PersonalizedEventsAdapter(context, eventWithDistance, listener, presenter);
        recyclerView = (RecyclerView) view;
        recyclerView.setLayoutManager(new GridLayoutManager(context, columnCount));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void showActionStatus(GenericQueryStatus genericQueryStatus, int position) {
        EventsViewHolder viewHolder = (EventsViewHolder) recyclerView.findViewHolderForAdapterPosition(position);
        viewHolder.showParticipationSavingStatus(genericQueryStatus);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PersonalizedEventsFragment.OnListFragmentInteractionListener) {
            listener = (PersonalizedEventsFragment.OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    public void sortEvents(SortTypes type) {
        presenter.onSortRequested(type);
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
