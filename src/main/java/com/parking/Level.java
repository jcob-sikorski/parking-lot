package com.parking;

import java.util.List;

public class Level {
    private final List<ParkingSpot> parkingSpots;
    private final int id;

    public Level(int id, List<ParkingSpot> parkingSpots) {
        this.id = id;
        this.parkingSpots = parkingSpots;
    }

    public List<ParkingSpot> getParkingSpots() {
        return parkingSpots;
    }

    public int getId() {
        return id;
    }
}