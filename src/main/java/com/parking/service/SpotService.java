package com.parking.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import com.parking.model.spot.Level;
import com.parking.model.spot.ParkingSpot;
import com.parking.model.spot.SpotType;
import com.parking.model.vehicle.Vehicle;
import com.parking.strategy.search.NearestFit;
import com.parking.strategy.search.SpotSearchStrategy;

public class SpotService {
    private final SpotSearchStrategy spotSearchStrategy;
    private final List<Level> levels;

    private final Map<String, ParkingSpot> spotMap;

    public SpotService() {
        this.spotSearchStrategy = new NearestFit();
        this.levels = new ArrayList<>();
        this.spotMap = new ConcurrentHashMap<>();

        initializeInventory();
    }

    private void initializeInventory() {
        System.out.println("Initializing Inventory (50 Levels, 10,000 Spots)...");

        for (int floor = 1; floor <= 50; floor++) {
            List<ParkingSpot> floorSpots = new ArrayList<>(200);

            for (int slot = 1; slot <= 200; slot++) {
                // Mix of spot types
                SpotType type;
                if (slot <= 150) type = SpotType.COMPACT;   // 75%
                else type = SpotType.LARGE;                 // 25%

                String id = "L" + floor + "-" + slot;
                ParkingSpot spot = new ParkingSpot(id, type);

                floorSpots.add(spot);
                spotMap.put(id, spot);
            }

            levels.add(new Level(floor, floorSpots));
        }
    }

    /**
     * CORE LOGIC: Finds a spot and atomically locks it.
     */
    public Optional<ParkingSpot> findAndBookSpot(Vehicle vehicle) {
        // Try to find a spot up to 3 times in case of race conditions
        for (int i = 0; i < 3; i++) {
            Optional<ParkingSpot> candidate = spotSearchStrategy.findAvailableSpot(levels, vehicle);

            if (candidate.isPresent()) {
                ParkingSpot spot = candidate.get();
                // Attempt to atomic lock
                if (spot.attemptPark(vehicle)) {
                    return Optional.of(spot); // Success!
                }
                // If we fail here, it means another thread stole the spot
                // between the 'find' line and the 'attemptPark' line.
                // We loop again to find a NEW spot.
            } else {
                // Strategy says lot is physically full
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

    /**
     * Frees a spot by ID in O(1) time.
     */
    public void freeSpot(String spotId) {
        if (spotId == null) return;

        ParkingSpot spot = spotMap.get(spotId);
        if (spot != null) {
            spot.vacate();
        } else {
            System.err.println("Error: Spot ID " + spotId + " not found!");
        }
    }

    public List<Level> getLevels() {
        return Collections.unmodifiableList(levels);
    }
}