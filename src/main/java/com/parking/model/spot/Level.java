package com.parking.model.spot;

import java.util.Collections;
import java.util.List;

public class Level {
    private final int floorNumber;
    private final List<ParkingSpot> spots;

    public Level(int floorNumber, List<ParkingSpot> spots) {
        this.floorNumber = floorNumber;
        this.spots = Collections.unmodifiableList(spots);
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public List<ParkingSpot> getSpots() {
        return spots;
    }

    /**
     * Helper method for Display Boards.
     * Calculates available spots in real-time.
     */
    public long getAvailableCount() {
        return spots.stream()
                    .filter(ParkingSpot::isFree)
                    .count();
    }
}