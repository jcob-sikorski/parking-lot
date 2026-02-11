package com.parking.strategy.pricing;

import com.parking.model.ticket.Ticket;

public interface PricingStrategy {
    /**
     * Calculates the fee based on the entry and exit times of the ticket.
     * @param ticket The ticket containing the duration data.
     * @return The calculated cost as a double.
     */
    double calculateFee(Ticket ticket);
}