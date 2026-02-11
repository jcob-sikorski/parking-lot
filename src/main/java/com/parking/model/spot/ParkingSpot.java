package com.parking.model.spot;

import java.util.concurrent.atomic.AtomicBoolean;
import com.parking.model.vehicle.Vehicle;

public class ParkingSpot {
    private final String id;
    private final SpotType spotType;

    private final AtomicBoolean isOccupied;

    private volatile Vehicle parkedVehicle;

    public ParkingSpot(String id, SpotType spotType) {
        this.id = id;
        this.spotType = spotType;

        this.isOccupied = new AtomicBoolean(false); 
    }

    /**
     * Thread-Safe Attempt to park.
     * @param vehicle The vehicle trying to park.
     * @return true if parking was successful, false if spot was already taken.
     */
    public boolean attemptPark(Vehicle vehicle) {
        // This prevents two vehicles from grabbing the spot at the exact same nanosecond.
        if (isOccupied.compareAndSet(false, true)) {
            this.parkedVehicle = vehicle;
            return true;
        }
        return false; // Spot was already taken
    }

    /**
     * Removes the vehicle and frees the spot.
     */
    public void vacate() {
        this.parkedVehicle = null;
        isOccupied.set(false);
    }

    public boolean isFree() {
        return !isOccupied.get();
    }

    public String getId() { return id; }

    public SpotType getType() { return spotType; }

    public Vehicle getParkedVehicle() { return parkedVehicle; }
}