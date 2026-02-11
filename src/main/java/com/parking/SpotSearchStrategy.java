package com.parking;

import java.util.List;
import java.util.Optional;

public interface SpotSearchStrategy {
    /**
     * Searches for a parking spot based on the specific strategy implementation.
     * @param levels The list of parking levels to search through.
     * @param vehicle The vehicle needing a spot (to check for size/type compatibility).
     * @return An Optional containing a spot if found, or empty if the lot is full.
     */
    Optional<ParkingSpot> findAvailableSpot(List<Level> levels, Vehicle vehicle);
}