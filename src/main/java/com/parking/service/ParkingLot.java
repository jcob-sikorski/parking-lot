package com.parking.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.parking.terminal.Terminal;
import com.parking.terminal.TerminalType;

public class ParkingLot {
    private static volatile ParkingLot instance;

    private final NotificationService notificationService;
    private final SpotService spotService;
    private final TicketService ticketService;

    private final List<Terminal> terminals;

    private ParkingLot() {
        System.out.println("Initializing Parking Lot System...");

        this.notificationService = new NotificationService();
        this.spotService = new SpotService();
        this.ticketService = new TicketService();

        this.terminals = new ArrayList<>(10000);

        for (int i = 1; i <= 10000; i++) {
            TerminalType type = (i % 2 == 0) ? TerminalType.EXIT : TerminalType.ENTRY;
            String terminalId = "T-" + i;

            Terminal terminal = new Terminal(terminalId, type);
            this.terminals.add(terminal);
        }
        System.out.println("Hardware initialization complete: " + terminals.size() + " terminals online.");
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

    // --- Service Getters ---

    public NotificationService getNotificationService() {
        return notificationService;
    }

    public SpotService getSpotService() { 
        return spotService;
    }

    public TicketService getTicketService() {
        return ticketService;
    }

    /**
     * Returns an unmodifiable view of the terminals list to prevent external modification.
     */
    public List<Terminal> getTerminals() {
        return Collections.unmodifiableList(terminals);
    }
}