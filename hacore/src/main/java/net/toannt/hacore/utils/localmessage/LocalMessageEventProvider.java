package net.toannt.hacore.utils.localmessage;

import io.reactivex.Observable;

import java.util.HashMap;
import java.util.Map;

public class LocalMessageEventProvider {

    private static Map<Class, RxEventBus> sMessageEventBusSet = new HashMap<>();

    static {
        RxEventBus<? extends LocalMessage> localMessage = RxEventBus.createBus();
        sMessageEventBusSet.put(LocalMessage.class, localMessage);
    }

    static <T> RxEventBus<T> getAllEventBus(Class<T> clazz) {
        return sMessageEventBusSet.get(clazz);
    }

    public static <T extends LocalMessage> Observable<T> localMessageObservable(Class<T> clazz) {
        return getAllEventBus(LocalMessage.class).toObservable()
                .filter(localMessage -> localMessage.getClass().equals(clazz))
                .ofType(clazz);
    }
}
