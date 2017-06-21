package pl.rmakowiecki.eventhub.ui.screen_event_details;

import pl.rmakowiecki.eventhub.api.EventImageStorageInteractor;
import pl.rmakowiecki.eventhub.repository.AddSpecificOperationRepository;
import pl.rmakowiecki.eventhub.repository.GenericQueryStatus;
import pl.rmakowiecki.eventhub.repository.QuerySingle;
import pl.rmakowiecki.eventhub.repository.Specification;
import rx.Observable;
import rx.subjects.PublishSubject;

public class EventImageRepository implements AddSpecificOperationRepository<byte[], GenericQueryStatus>, QuerySingle<byte[]> {

    private EventImageStorageInteractor eventImageStorageInteractor;
    PublishSubject<byte[]> publishSubject = PublishSubject.create();

    public EventImageRepository() {
        eventImageStorageInteractor = new EventImageStorageInteractor();
    }

    @Override
    public Observable<byte[]> querySingle(Specification specification) {
        EventImageSpecification eventImageSpecification = (EventImageSpecification) specification;

        eventImageStorageInteractor
                .getData(eventImageSpecification.getFirebaseKey())
                .subscribe(data -> dataArrived(data));

        return publishSubject;
    }

    private void dataArrived(byte[] data) {
        publishSubject.onNext(data);
    }

    @Override
    public Observable<GenericQueryStatus> add(String key, byte[] item) {
        return eventImageStorageInteractor.add(key, item);
    }

    @Override
    public Observable<GenericQueryStatus> add(String key, Iterable<byte[]> items) {
        return Observable.empty();
    }
}
