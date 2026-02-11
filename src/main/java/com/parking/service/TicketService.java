package com.parking.service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.time.LocalDateTime;

import com.parking.model.vehicle.Vehicle;
import com.parking.model.ticket.Ticket;
import com.parking.model.ticket.TicketStatus;
import com.parking.model.spot.ParkingSpot;
import com.parking.strategy.pricing.PricingStrategy;
import com.parking.strategy.pricing.FixedFee;

public class TicketService {
    private final Map<String, Ticket> activeTickets;
    private final PricingStrategy pricingStrategy;

    public TicketService() {
        this.activeTickets = new HashMap<>();
        this.pricingStrategy = new FixedFee();
    }

    public Ticket generateTicket(Vehicle vehicle, ParkingSpot spot) {
        String ticketId = "TIC-" + UUID.randomUUID().toString().substring(0, 8);
    
        Ticket ticket = new Ticket(ticketId, vehicle, spot.getId());

        activeTickets.put(ticketId, ticket);
        return ticket;
    }

    public double processExit(Ticket ticket) {
        if (ticket.getStatus() != TicketStatus.ACTIVE) {
            throw new IllegalStateException("Ticket is already processed or invalid.");
        }

        ticket.setExitTime(LocalDateTime.now());
        double fee = pricingStrategy.calculateFee(ticket);

        ticket.setAmount(fee);
        ticket.setStatus(TicketStatus.PAID);

        activeTickets.remove(ticket.getTicketNumber());

        return fee;
    }

    public Ticket getTicket(String ticketNumber) {
        return activeTickets.get(ticketNumber);
    }
}