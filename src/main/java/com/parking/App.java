package com.parking;

public class App {
    public static void main(String[] args) {
        System.out.println("Parking System Online - Java 25");
        ParkingLot parkingSystem = ParkingLot.getInstance();

        if (parkingSystem != null) {
            System.out.println("Parking Lot initialized successfully.");
        }
    }
}