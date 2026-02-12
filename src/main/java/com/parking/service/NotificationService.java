package com.parking.service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import com.parking.observer.Subscriber;
import com.parking.model.ParkingContext;

public class NotificationService {
    private final List<Subscriber> subscribers = new CopyOnWriteArrayList<>();

    public void subscribe(Subscriber s) { subscribers.add(s); }
    public void unsubscribe(Subscriber s) { subscribers.remove(s); }

    public void notifySubscribers(ParkingContext context) {
        for (Subscriber s : subscribers) {
            s.update(context);
        }
    }
}