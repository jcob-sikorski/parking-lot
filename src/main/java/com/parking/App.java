package com.parking;

import java.util.concurrent.*;
import java.util.concurrent.atomic.LongAdder;
import java.util.Optional;

import com.parking.service.ParkingLot;
import com.parking.model.vehicle.Car;
import com.parking.model.spot.ParkingSpot;
import com.parking.model.ticket.Ticket;

public class App {
    private static final LongAdder parkedCount = new LongAdder();
    private static final LongAdder totalWaitTime = new LongAdder();
    
    // REDUCED: 1 Million cars (Finishes in ~20 seconds)
    private static final int TOTAL_CARS = 1_000_000;

    // PHYSICAL LIMIT: 10,000 Spots
    private static final Semaphore lotGate = new Semaphore(10_000, true);

    // INCREASED BACKPRESSURE: Allow massive queue build-up (500k waiting)
    private static final Semaphore systemCapacity = new Semaphore(500_000);

    public static void main(String[] args) throws InterruptedException {
        ParkingLot system = ParkingLot.getInstance();
        System.out.println("Starting Burst Mode Simulation...");
        
        startLiveDashboard();

        long startTime = System.currentTimeMillis();

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < TOTAL_CARS; i++) {
                // Main thread creates cars FASTER than they can park
                systemCapacity.acquire();
                int carId = i;
                executor.submit(() -> {
                    try {
                        simulateCarLifecycle(system, carId);
                    } finally {
                        systemCapacity.release();
                    }
                });
            }
        }

        long duration = System.currentTimeMillis() - startTime;
        printSummary(duration);
    }

    private static void simulateCarLifecycle(ParkingLot system, int carId) {
        long requestTime = System.currentTimeMillis();
        var vehicle = new Car("ABC-" + carId);

        try {
            lotGate.acquire(); // Cars will pile up here!
            
            // Track wait time
            totalWaitTime.add(System.currentTimeMillis() - requestTime);

            Optional<ParkingSpot> spotOpt = system.getSpotService().findAndBookSpot(vehicle);

            if (spotOpt.isPresent()) {
                ParkingSpot spot = spotOpt.get();
                parkedCount.increment();
                Ticket ticket = system.getTicketService().generateTicket(vehicle, spot);

                try {
                    // FAST PARK: 1ms (High throughput)
                    Thread.sleep(1); 
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                system.getTicketService().processExit(ticket);
                system.getSpotService().freeSpot(spot.getId());
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lotGate.release();
        }
    }

    private static void startLiveDashboard() {
        Thread.ofVirtual().start(() -> {
            long lastParked = 0;
            long lastCheck = System.currentTimeMillis();

            while (parkedCount.sum() < TOTAL_CARS) {
                long currentParked = parkedCount.sum();
                long currentTime = System.currentTimeMillis();
                double deltaSeconds = (currentTime - lastCheck) / 1000.0;
                double rate = (deltaSeconds > 0) ? (currentParked - lastParked) / deltaSeconds : 0;
                
                lastParked = currentParked;
                lastCheck = currentTime;

                int waitingInQueue = lotGate.getQueueLength(); 
                int occupiedSpots = 10_000 - lotGate.availablePermits();
                double progress = (currentParked / (double) TOTAL_CARS) * 100;
                
                System.out.printf("\rProgress: [%-20s] %5.1f%% | Rate: %6.0f/s | Active: %5d | Queue: %6d | Total: %d",
                    "=".repeat((int) (progress / 5)), progress, rate, occupiedSpots, waitingInQueue, currentParked);

                try { Thread.sleep(200); } catch (InterruptedException e) { break; }
            }
        });
    }

    private static void printSummary(long duration) {
        long totalParked = parkedCount.sum();
        double throughput = (duration > 0) ? totalParked / (duration / 1000.0) : 0;
        System.out.println("\n\n--- Final Results ---");
        System.out.println("Successful Parkings: " + totalParked);
        System.out.println("Throughput: " + String.format("%.2f", throughput) + " cars/sec");
    }
}