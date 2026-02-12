package com.parking.service;

import java.util.concurrent.ConcurrentHashMap;
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
    private final Map<String, Ticket> activeTickets = new ConcurrentHashMap<>();
    private final PricingStrategy pricingStrategy = new FixedFee();

    public Ticket generateTicket(Vehicle vehicle, ParkingSpot spot) {
        String ticketId = "TIC-" + UUID.randomUUID().toString().substring(0, 8);

        Ticket ticket = new Ticket(ticketId, vehicle, spot.getId());

        activeTickets.put(ticketId, ticket);
        return ticket;
    }

    public double processExit(Ticket ticket) {
        Ticket removed = activeTickets.remove(ticket.getTicketNumber());
        
        if (removed == null || removed.getStatus() != TicketStatus.ACTIVE) {
            throw new IllegalStateException("Ticket already processed.");
        }

        removed.setExitTime(LocalDateTime.now());
        double fee = pricingStrategy.calculateFee(removed);
        removed.setAmount(fee);
        removed.setStatus(TicketStatus.PAID);

        return fee;
    }
}