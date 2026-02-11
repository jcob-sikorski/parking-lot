package com.parking;

public class ParkingLot {
    private static ParkingLot instance;

    // TODO: Implement actual service logic for the following fields
    private final NotificationService notificationService;
    private final SpotService spotService;
    private final TicketService ticketService;

    private ParkingLot() {
        // TODO: Replace with dependency injection or proper initialization logic
        this.notificationService = new NotificationService();
        this.spotService = new SpotService();
        this.ticketService = new TicketService();
    }

    /**
     * Returns the singleton instance.
     * Note: Current implementation is not thread-safe for multi-threaded environments.
     */
    public static ParkingLot getInstance() {
        if (instance == null) {
            instance = new ParkingLot();
        }
        return instance;
    }
}