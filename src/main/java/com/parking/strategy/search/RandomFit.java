package com.parking.strategy.search;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import com.parking.model.vehicle.Vehicle;
import com.parking.model.spot.Level;
import com.parking.model.spot.ParkingSpot;

public class RandomFit implements SpotSearchStrategy {

    @Override
    public Optional<ParkingSpot> findAvailableSpot(List<Level> levels, Vehicle vehicle) {
        if (levels == null || levels.isEmpty()) return Optional.empty();

        int numLevels = levels.size();
        // Start at a random floor to distribute thread load across the lot
        int startFloor = ThreadLocalRandom.current().nextInt(numLevels);

        for (int i = 0; i < numLevels; i++) {
            // Use modulo to wrap around and ensure all floors are eventually checked
            int currentFloorIndex = (startFloor + i) % numLevels;
            Level level = levels.get(currentFloorIndex);

            // Within the level, we also randomize the starting spot to prevent
            // many threads from hitting the first spot of the same floor simultaneously
            List<ParkingSpot> spots = level.getSpots();
            int numSpots = spots.size();
            int startSpot = ThreadLocalRandom.current().nextInt(numSpots);

            for (int j = 0; j < numSpots; j++) {
                int currentSpotIndex = (startSpot + j) % numSpots;
                ParkingSpot spot = spots.get(currentSpotIndex);

                if (spot.isFree() && spot.getType().fits(vehicle.getType())) {
                    return Optional.of(spot);
                }
            }
        }
        return Optional.empty();
    }
}