package com.parking.model.vehicle;

public abstract class Vehicle {
    private final String licenseNumber;

    protected Vehicle(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public abstract VehicleType getType();
}