package com.parking.model.spot;

import com.parking.model.vehicle.VehicleType;

public enum SpotType {
    COMPACT, // For Cars
    LARGE;   // For Trucks

    /**
     * Determines if a vehicle can fit in this spot type.
     * Logic:
     * - LARGE spots fit everything (Trucks and Cars).
     * - COMPACT spots fit Cars, but not Trucks.
     */
    public boolean fits(VehicleType vehicleType) {
        switch (this) {
            case LARGE:
                // Large spots are universal for the current vehicle list
                return true;
            case COMPACT:
                // Only Cars fit in Compact spots; Trucks are too big
                return vehicleType == VehicleType.CAR;
            default:
                return false;
        }
    }
}