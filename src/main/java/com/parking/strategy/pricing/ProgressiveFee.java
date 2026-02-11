package com.parking.strategy.pricing;

import com.parking.model.ticket.Ticket;

public class ProgressiveFee implements PricingStrategy {
    // TODO: Move these to a config file or database
    private static final double BASE_RATE = 5.0; // First hour
    private static final double INCREMENTAL_RATE = 2.0; // Every hour after

    @Override
    public double calculateFee(Ticket ticket) {
        // TODO: 1. Calculate duration using ticket.getEntryTime() and LocalDateTime.now()
        // TODO: 2. Apply BASE_RATE for the first hour
        // TODO: 3. Apply INCREMENTAL_RATE for additional hours
        return 0.0;
    }
}