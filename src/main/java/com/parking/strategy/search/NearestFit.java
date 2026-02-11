package com.parking.strategy.search;

import java.util.List;
import java.util.Optional;
import com.parking.model.vehicle.Vehicle;
import com.parking.model.spot.Level;
import com.parking.model.spot.ParkingSpot;

public class NearestFit implements SpotSearchStrategy {
    
    @Override
    public Optional<ParkingSpot> findAvailableSpot(List<Level> levels, Vehicle vehicle) {
        for (Level level : levels) {
            for (ParkingSpot spot : level.getSpots()) {
                if (spot.isFree() && spot.getType().fits(vehicle.getType())) {
                    return Optional.of(spot);
                }
            }
        }
        return Optional.empty();
    }
}