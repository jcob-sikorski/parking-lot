package com.parking.model;

public class ParkingContext {
    private final int availableSpots;

    public ParkingContext(int availableSpots) {
        this.availableSpots = availableSpots;
    }

    public int getAvailableSpots() {
        return availableSpots;
    }
}