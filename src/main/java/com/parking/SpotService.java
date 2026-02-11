package com.parking;

import java.util.ArrayList;
import java.util.List;

public class SpotService {
    // TODO: Implement actual searching logic (e.g., Nearest First, Handicapped Accessible First)
    private final SpotSearchStrategy spotSearchStrategy;
    
    // TODO: Consider a Repository pattern if levels are stored in a database
    private final List<Level> levels;

    public SpotService() {
        // TODO: Replace with dependency injection or proper strategy selection
        this.spotSearchStrategy = new SpotSearchStrategy();
        this.levels = new ArrayList<>();
    }
}