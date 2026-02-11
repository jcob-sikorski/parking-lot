package com.parking.service;

import java.util.HashMap;
import java.util.Map;

import com.parking.model.vehicle.Vehicle;
import com.parking.model.ticket.Ticket;

import com.parking.strategy.pricing.PricingStrategy;

public class TicketService {
    // TODO: Implement persistent storage or a repository for tickets
    private final Map<Vehicle, Ticket> tickets;

    // TODO: Define specific pricing strategies (e.g., Fixed, Progressive)
    private final PricingStrategy pricingStrategy;

    public TicketService() {
        // TODO: Replace with dependency injection or proper strategy selection
        this.tickets = new HashMap<>();
        this.pricingStrategy = null;
    }

    public Ticket issueTicket(Vehicle vehicle) {
        // TODO: Create a new Ticket with a timestamp and add it to the 'tickets' map
        return null;
    }

    public void validateTicket(Ticket ticket) {
        // TODO: Use the pricingStrategy to calculate duration/cost and process payment
    }
}