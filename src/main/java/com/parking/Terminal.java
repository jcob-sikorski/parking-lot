package com.parking;

public class Terminal {
    private final String id;
    private final TerminalType type;
    private final NotificationService notificationService;

    // In a real app, this would be a shared ParkingLot object, not an int
    private static int currentSpots = 50;

    public Terminal(String id, TerminalType type, NotificationService service) {
        this.id = id;
        this.type = type;
        this.notificationService = service;
    }

    public void processVehicle() {
        if (type == TerminalType.ENTRY) {
            enter();
        } else {
            exit();
        }
    }

    private void enter() {
        if (currentSpots > 0) {
            currentSpots--;
            System.out.println("Entry at " + id + ". Spots left: " + currentSpots);
            notifyUpdate();
        } else {
            System.out.println("Parking Full!");
        }
    }

    private void exit() {
        currentSpots++;
        System.out.println("Exit at " + id + ". Spots left: " + currentSpots);
        notifyUpdate();
    }

    private void notifyUpdate() {
        notificationService.notifySubscribers(new ParkingContext(currentSpots));
    }
}