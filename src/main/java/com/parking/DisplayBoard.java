package com.parking;

public class DisplayBoard implements Subscriber {

    @Override
    public void update(ParkingContext context) {
        // Implementation of the update method
        displayAvailableSpotsNumber(context.getAvailableSpots());
    }

    public void displayAvailableSpotsNumber(int number) {
        System.out.println("DisplayBoard: Available parking spots: " + number);
    }
}