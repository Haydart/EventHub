package pl.rmakowiecki.eventhub.ui.screen_event_calendar;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import pl.rmakowiecki.eventhub.util.SortTypes;
import pl.rmakowiecki.eventhub.util.UserAuthManager;

public class MyEventsFragment extends BaseFragment<MyEventsFragmentPresenter> implements MyEventsFragmentView {

    public static final String ARG_PAGE = "ARG_PAGE";
    private static final int PAGE = 1;
    @BindView(R.id.loading_panel) RelativeLayout loadingPanel;
    @BindView(R.id.not_logged_panel) LinearLayout notLoggedPanel;
    @BindView(R.id.no_my_events_panel) LinearLayout noEventsPanel;
    @BindView(R.id.calendar_events_list) RecyclerView recyclerView;
    @BindString(R.string.status_success_message) String successMessage;
    @BindString(R.string.status_fail_message) String failMessage;
    @BindString(R.string.status_leave_success_message) String leaveSuccessMessage;
    @BindString(R.string.status_leave_fail_message) String leaveFailMessage;
    private UserAuthManager userAuthManager = new UserAuthManager();
    private int columnCount = 1;
    private OnListFragmentInteractionListener listener;
    private RecyclerView.Adapter adapter;
    private View view;

    public MyEventsFragment() {
        //no-op
    }

    public static MyEventsFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        MyEventsFragment fragment = new MyEventsFragment();
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
        view = inflater.inflate(R.layout.fragment_my_events_list, container, false);
        ButterKnife.bind(this, view);
        showPlaceholder();
        return view;
    }

    @Override
    public void showPlaceholder() {
        loadingPanel.setVisibility(View.GONE);
        notLoggedPanel.setVisibility(View.VISIBLE);
    }

    @Override
    public void showNoEventsPanel() {
        loadingPanel.setVisibility(View.GONE);
        notLoggedPanel.setVisibility(View.GONE);
        noEventsPanel.setVisibility(View.VISIBLE);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_events_list;
    }

    @Override
    protected void initPresenter() {
        presenter = new MyEventsFragmentPresenter(PAGE);
    }

    @Override
    public void showEvents(List<EventWDistance> eventWithDistance, List<Boolean> attendance) {
        initEvents(eventWithDistance, attendance);
    }

    @Override
    public void initEvents(List<EventWDistance> eventWithDistance, List<Boolean> attendance) {
        Context context = getContext();
        adapter = new MyEventsAdapter(context, eventWithDistance, attendance, listener, presenter);
        recyclerView.setLayoutManager(new GridLayoutManager(context, columnCount));
        recyclerView.setAdapter(adapter);
        notLoggedPanel.setVisibility(View.GONE);
        noEventsPanel.setVisibility(View.GONE);
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
