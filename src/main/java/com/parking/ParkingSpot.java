package com.parking;

public class ParkingSpot {
    private boolean isAvailable;
    private Vehicle parkedVehicle;
    private final SpotType spotType;
    private final int id;

    public ParkingSpot(SpotType spotType, int id) {
        this.isAvailable = true;
        this.parkedVehicle = null;
        this.spotType = spotType;
        this.id = id;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void parkVehicle(Vehicle vehicle) {
        this.parkedVehicle = vehicle;
        this.isAvailable = false;
    }

    public void vacate() {
        this.parkedVehicle = null;
        this.isAvailable = true;
    }

    public SpotType getSpotType() {
        return spotType;
    }

    public int getId() {
        return id;
    }
}