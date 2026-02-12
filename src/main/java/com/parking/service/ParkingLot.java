package com.parking.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue; // Thread-safe queue
import com.parking.terminal.Terminal;
import com.parking.terminal.TerminalType;
import com.parking.model.vehicle.Vehicle;

public class ParkingLot {
    private static volatile ParkingLot instance;

    private final NotificationService notificationService;
    private final SpotService spotService;
    private final TicketService ticketService;
    private final List<Terminal> terminals;

    // The Entry Queue: Thread-safe and non-blocking for producers
    private final LinkedBlockingQueue<Vehicle> entryQueue;

    private ParkingLot() {
        System.out.println("Initializing Parking Lot System...");

        this.notificationService = new NotificationService();
        this.spotService = new SpotService();
        this.ticketService = new TicketService();
        this.entryQueue = new LinkedBlockingQueue<>(); // Unbounded for simulation

        this.terminals = new ArrayList<>(10000);
        for (int i = 1; i <= 10000; i++) {
            TerminalType type = (i % 2 == 0) ? TerminalType.EXIT : TerminalType.ENTRY;
            this.terminals.add(new Terminal("T-" + i, type));
        }
        System.out.println("Hardware initialization complete: 10000 terminals online.");
    }

    public static ParkingLot getInstance() {
        if (instance == null) {
            synchronized (ParkingLot.class) {
                if (instance == null) {
                    instance = new ParkingLot();
                }
            }
        }
        return instance;
    }

    /**
     * Vehicles "enter" the system here if no spots are immediately available.
     */
    public void enqueueVehicle(Vehicle vehicle) {
        entryQueue.offer(vehicle);
    }

    public LinkedBlockingQueue<Vehicle> getEntryQueue() {
        return entryQueue;
    }

    // --- Service Getters ---
    public NotificationService getNotificationService() { return notificationService; }
    public SpotService getSpotService() { return spotService; }
    public TicketService getTicketService() { return ticketService; }
    public List<Terminal> getTerminals() { return Collections.unmodifiableList(terminals); }
}