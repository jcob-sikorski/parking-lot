package com.parking.strategy.search;

import java.util.List;
import java.util.Optional;

import com.parking.model.vehicle.Vehicle;
import com.parking.model.spot.Level;

import com.parking.model.spot.ParkingSpot;

public class NearestFit implements SpotSearchStrategy {
    
    @Override
    public Optional<ParkingSpot> findAvailableSpot(List<Level> levels, Vehicle vehicle) {
        // 1. Iterate through levels
        for (Level level : levels) {

            // 2. Iterate through the spots in that level MANUALLY
            for (ParkingSpot spot : level.getParkingSpots()) {

                // 3. Check if this spot works
                if (spot.isAvailable() && spot.getSpotType().fits(vehicle.getType())) {
                    return Optional.of(spot);
                }
            }
        }

        // 4. No spot found anywhere
        return Optional.empty();
    }
}