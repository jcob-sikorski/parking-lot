package com.parking;

public class ParkingLot {
    private static ParkingLot instance;

    private ParkingLot() {
        // We will initialize services here (SpotService, etc.)
    }

    public static ParkingLot getInstance() {
        if (instance == null) {
            instance = new ParkingLot();
        }
        return instance;
    }
}