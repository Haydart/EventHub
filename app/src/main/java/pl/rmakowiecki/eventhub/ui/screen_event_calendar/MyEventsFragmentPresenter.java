package pl.rmakowiecki.eventhub.ui.screen_event_calendar;

import pl.rmakowiecki.eventhub.model.local.Event;
import pl.rmakowiecki.eventhub.repository.Repository;
import pl.rmakowiecki.eventhub.ui.BasePresenter;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by m1per on 18.04.2017.
 */

public class MyEventsFragmentPresenter extends BasePresenter<MyEventsFragmentView> {

    private Repository<Event> repository;
    private int position;

    MyEventsFragmentPresenter(int position) {
        repository = new EventsRepository();
        this.position = position;
    }

    private void onViewInitialization() {
        repository
                .query(new MyEventsSpecifications(position) {
                })
                .compose(applySchedulers())
                .subscribe(view::showEvents);
    }

    @Override
    protected void onViewStarted(MyEventsFragmentView view) {
        super.onViewStarted(view);
        onViewInitialization();
    }

    @Override
    public MyEventsFragmentView getNoOpView() {
        return NoOpMyEventsFragmentView.INSTANCE;
    }
}
