package com.parking.service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import com.parking.model.spot.*;
import com.parking.model.vehicle.Vehicle;
import com.parking.strategy.search.*;

public class SpotService {
    private final SpotSearchStrategy spotSearchStrategy;
    private final List<Level> levels;
    private final Map<String, ParkingSpot> spotMap = new ConcurrentHashMap<>();

    public SpotService() {
        this.spotSearchStrategy = new RandomFit();
        List<Level> tempLevels = new ArrayList<>();
        initializeInventory(tempLevels);
        this.levels = Collections.unmodifiableList(tempLevels);
    }

    private void initializeInventory(List<Level> tempLevels) {
        System.out.println("Initializing Inventory (50 Levels, 10,000 Spots)...");
        for (int floor = 1; floor <= 50; floor++) {
            List<ParkingSpot> floorSpots = new ArrayList<>(200);
            for (int slot = 1; slot <= 200; slot++) {
                SpotType type = (slot <= 150) ? SpotType.COMPACT : SpotType.LARGE;
                String id = "L" + floor + "-" + slot;
                ParkingSpot spot = new ParkingSpot(id, type);

                floorSpots.add(spot);
                spotMap.put(id, spot);
            }
            tempLevels.add(new Level(floor, Collections.unmodifiableList(floorSpots)));
        }
    }

    public Optional<ParkingSpot> findAndBookSpot(Vehicle vehicle) {
        for (int i = 0; i < 5; i++) {
            Optional<ParkingSpot> candidate = spotSearchStrategy.findAvailableSpot(levels, vehicle);
            if (candidate.isPresent()) {
                if (candidate.get().attemptPark(vehicle)) {
                    return candidate;
                }
            } else {
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

    public void freeSpot(String spotId) {
        Optional.ofNullable(spotMap.get(spotId)).ifPresent(ParkingSpot::vacate);
    }
}