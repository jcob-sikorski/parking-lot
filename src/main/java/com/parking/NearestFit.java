package com.parking;

import java.util.List;
import java.util.Optional;

public class NearestFit implements SpotSearchStrategy {
    
    @Override
    public Optional<ParkingSpot> findAvailableSpot(List<Level> levels, Vehicle vehicle) {
        // TODO: Iterate through levels starting from the entrance (e.g., floor 0)
        // TODO: Within each level, find the first available spot that fits the vehicle type
        // TODO: Return Optional.of(spot) if found, otherwise Optional.empty()
        return Optional.empty();
    }
}