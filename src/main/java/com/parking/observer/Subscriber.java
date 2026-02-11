package com.parking.observer;

import com.parking.model.ParkingContext;

public interface Subscriber {
    void update(ParkingContext context);
}