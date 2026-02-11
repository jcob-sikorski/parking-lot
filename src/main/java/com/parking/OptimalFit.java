package com.parking;

import java.util.List;
import java.util.Optional;

public class OptimalFit implements SpotSearchStrategy {
    
    @Override
    public Optional<ParkingSpot> findAvailableSpot(List<Level> levels, Vehicle vehicle) {
        // TODO: Implement "Optimal" logic (e.g., finding a spot that minimizes walking distance
        // or fits the vehicle size perfectly to avoid wasting large spots on small cars)
        
        return Optional.empty();
    }
}