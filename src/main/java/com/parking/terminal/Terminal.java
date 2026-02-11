package com.parking.terminal;

import com.parking.service.ParkingLot;
import com.parking.service.SpotService;
import com.parking.service.TicketService;
import com.parking.model.spot.ParkingSpot;
import com.parking.model.ticket.Ticket;
import com.parking.model.vehicle.Vehicle;

import java.util.Optional;

public class Terminal {
    private final String id;
    private final TerminalType type;

    public Terminal(String id, TerminalType type) {
        this.id = id;
        this.type = type;
    }

    /**
     * ENTRY FLOW: User drives up, camera scans vehicle.
     */
    public void processEntry(Vehicle vehicle) {
        if (type != TerminalType.ENTRY) {
            System.out.println("Error: Cannot process entry at an EXIT terminal.");
            return;
        }

        ParkingLot system = ParkingLot.getInstance();
        SpotService spotService = system.getSpotService();
        TicketService ticketService = system.getTicketService();

        Optional<ParkingSpot> spot = spotService.findAndBookSpot(vehicle);

        if (spot.isPresent()) {
            Ticket ticket = ticketService.generateTicket(vehicle, spot.get());

            System.out.println("------------------------------------------------");
            System.out.println("Entry Gate " + id + " OPEN.");
            System.out.println("Vehicle: " + vehicle.getLicenseNumber() + " (" + vehicle.getType() + ")");
            System.out.println("Assigned Spot: " + spot.get().getId());
            System.out.println("Ticket Issued: " + ticket.getTicketNumber());
            System.out.println("------------------------------------------------");
        } else {
            System.out.println("Entry Gate " + id + " CLOSED. No suitable spots for " + vehicle.getType());
        }
    }

    /**
     * EXIT FLOW: User inserts ticket.
     */
    public void processExit(Ticket ticket) {
        if (type != TerminalType.EXIT) {
            System.out.println("Error: Cannot process exit at an ENTRY terminal.");
            return;
        }

        ParkingLot system = ParkingLot.getInstance();
        SpotService spotService = system.getSpotService();
        TicketService ticketService = system.getTicketService();

        double fee = ticketService.processExit(ticket);

        spotService.freeSpot(ticket.getAssignedSpotId());

        System.out.println("------------------------------------------------");
        System.out.println("Exit Gate " + id + " OPEN.");
        System.out.println("Ticket: " + ticket.getTicketNumber());
        System.out.println("Duration: " + ticket.getDurationInHours() + " hours");
        System.out.println("Total Fee: $" + String.format("%.2f", fee));
        System.out.println("Goodbye!");
        System.out.println("------------------------------------------------");
    }
}