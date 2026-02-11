package com.parking;

public class Car extends Vehicle {
    public Car(String licenseNumber) {
        super(licenseNumber);
    }

    @Override
    public VehicleType getType() {
        return VehicleType.CAR;
    }
}