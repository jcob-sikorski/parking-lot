package com.parking.service;

public class ParkingLot {
    // 1. 'volatile' ensures changes to this variable are immediately visible to other threads
    private static volatile ParkingLot instance;

    private final NotificationService notificationService;
    private final SpotService spotService;
    private final TicketService ticketService;

    private ParkingLot() {
        // ideally, use a Factory or Dependency Injection here, but for now:
        this.notificationService = new NotificationService();
        this.spotService = new SpotService();
        this.ticketService = new TicketService();
    }

    public static ParkingLot getInstance() {
        // 2. First check (no locking) - for performance
        if (instance == null) {
            // 3. Lock only if instance seems null
            synchronized (ParkingLot.class) {
                // 4. Second check - ensure another thread didn't create it while we waited for the lock
                if (instance == null) {
                    instance = new ParkingLot();
                }
            }
        }
        return instance;
    }

    // Getters for your services so App.java can use them
    public SpotService getSpotService() { return spotService; }
    // ... other getters
}