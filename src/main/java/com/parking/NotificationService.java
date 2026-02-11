package com.parking;

import java.util.ArrayList;
import java.util.List;

public class NotificationService {
    private final List<Subscriber> subscribers;

    public NotificationService() {
        this.subscribers = new ArrayList<>();
    }

    public void subscribe(Subscriber s) {
        subscribers.add(s);
    }

    public void unsubscribe(Subscriber s) {
        subscribers.remove(s);
    }

    public void notifySubscribers(ParkingContext context) {
        for (Subscriber s : subscribers) {
            s.update(context);
        }
    }
}