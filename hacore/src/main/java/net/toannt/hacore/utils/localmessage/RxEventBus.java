package net.toannt.hacore.utils.localmessage;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class RxEventBus<T> {
    private final Subject bus = PublishSubject.create().toSerialized();

    public static RxEventBus createBus() {
        return new RxEventBus();
    }

    public void post(T event) {
        bus.onNext(event);
    }

    public Observable<T> toObservable() {
        return bus;
    }

    public boolean hasObservers() {
        return bus.hasObservers();
    }
}
