package com.parking.model.vehicle;

public class Truck extends Vehicle {
    public Truck(String licenseNumber) {
        super(licenseNumber);
    }

    @Override
    public VehicleType getType() {
        return VehicleType.TRUCK;
    }
}