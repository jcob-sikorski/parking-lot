package com.parking;

public class FixedFee implements PricingStrategy {
    // TODO: Move this to a configuration file or database later
    private static final double FLAT_RATE = 10.0;

    @Override
    public double calculateFee(Ticket ticket) {
        // TODO: Implement logic to ensure fee is only charged if the ticket is valid
        // For now, returning a fixed constant
        return FLAT_RATE;
    }
}