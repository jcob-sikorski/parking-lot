package com.parking;

import com.parking.service.ParkingLot;

public class App {
    public static void main(String[] args) {
        System.out.println("Parking System Online - Java 25");
        ParkingLot parkingSystem = ParkingLot.getInstance();

        if (parkingSystem != null) {
            System.out.println("Parking Lot initialized successfully.");
        }
    }
}