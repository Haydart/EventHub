package pl.rmakowiecki.eventhub.ui.screen_event_calendar;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import java.util.List;
import pl.rmakowiecki.eventhub.R;
import pl.rmakowiecki.eventhub.model.local.Event;
import pl.rmakowiecki.eventhub.model.local.EventWDistance;
import pl.rmakowiecki.eventhub.repository.GenericQueryStatus;
import pl.rmakowiecki.eventhub.ui.BaseFragment;
import pl.rmakowiecki.eventhub.util.PreferencesManager;
import pl.rmakowiecki.eventhub.util.SortTypes;

public class EventsFragment extends BaseFragment<EventsFragmentPresenter> implements EventsFragmentView {

    public static final String ARG_PAGE = "ARG_PAGE";
    private static final int PAGE = 2;
    @BindView(R.id.loading_panel) RelativeLayout loadingPanel;
    @BindView(R.id.calendar_events_list) RecyclerView recyclerView;
    @BindString(R.string.status_success_message) String successMessage;
    @BindString(R.string.status_fail_message) String failMessage;
    @BindString(R.string.status_leave_success_message) String leaveSuccessMessage;
    @BindString(R.string.status_leave_fail_message) String leaveFailMessage;
    private int columnCount = 1;
    private OnListFragmentInteractionListener listener;
    private EventsAdapter adapter;
    private View view;
    private PreferencesManager manager;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_events_list, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_events_list;
    }

    @Override
    protected void initPresenter() {
        presenter = new EventsFragmentPresenter(PAGE, manager.getUserName());
    }

    @Override
    public void showEvents(List<EventWDistance> eventsWDistance, List<Boolean> attendance) {
        initEvents(eventsWDistance, attendance);
    }

    @Override
    public void initEvents(List<EventWDistance> eventsWDistances, List<Boolean> attendance) {
        Context context = getContext();
        adapter = new EventsAdapter(context, eventsWDistances, attendance, listener, presenter);
        recyclerView.setLayoutManager(new GridLayoutManager(context, columnCount));
        recyclerView.setAdapter(adapter);
        loadingPanel.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showActionStatus(GenericQueryStatus status) {
        Toast.makeText(getContext(), status == GenericQueryStatus.STATUS_SUCCESS ? successMessage : failMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLeaveActionStatus(GenericQueryStatus status) {
        Toast.makeText(getContext(), status == GenericQueryStatus.STATUS_SUCCESS ? leaveSuccessMessage : leaveFailMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        manager = new PreferencesManager(context);
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
