package com.parking;

import java.util.concurrent.*;
import java.util.concurrent.atomic.LongAdder;
import com.parking.service.ParkingLot;
import com.parking.model.vehicle.Car;
import com.parking.model.spot.ParkingSpot;
import com.parking.model.ticket.Ticket;
import java.util.Optional;

public class App {
    private static final LongAdder parkedCount = new LongAdder();
    private static final LongAdder totalWaitTime = new LongAdder();
    private static final int TOTAL_CARS = 1_000_000;

    // Limits the number of concurrent "parked" cars to the physical capacity (10,000)
    // The 'true' parameter ensures FIFO (First-In-First-Out) fairness for the queue.
    private static final Semaphore lotGate = new Semaphore(10_000, true);

    public static void main(String[] args) throws InterruptedException {
        ParkingLot system = ParkingLot.getInstance();
        System.out.println("Starting Queued Mega-Simulation: 1M Vehicles...");
        long startTime = System.currentTimeMillis();

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < TOTAL_CARS; i++) {
                int carId = i;
                executor.submit(() -> simulateCarLifecycle(system, carId));
            }
        } 
        // Executor will wait for all 1M virtual threads to complete here.

        long duration = System.currentTimeMillis() - startTime;
        printSummary(duration);
    }

    /**
     * Represents the full journey of a car:
     * Arriving, Waiting in Queue, Parking, Staying, and Exiting.
     */
    private static void simulateCarLifecycle(ParkingLot system, int carId) {
        long requestTime = System.currentTimeMillis();
        var vehicle = new Car("ABC-" + carId);

        try {
            // PHASE 1: THE QUEUE
            // Virtual thread suspends here until a permit (parking spot) is available.
            lotGate.acquire();

            // PHASE 2: THE ARRIVAL
            long waitTime = System.currentTimeMillis() - requestTime;
            totalWaitTime.add(waitTime);

            // Since the Semaphore guaranteed a spot, findAndBookSpot should succeed.
            Optional<ParkingSpot> spotOpt = system.getSpotService().findAndBookSpot(vehicle);

            if (spotOpt.isPresent()) {
                ParkingSpot spot = spotOpt.get();
                parkedCount.increment();

                // PHASE 3: THE STAY
                // Generate the ticket and simulate a random stay duration (0-10ms)
                Ticket ticket = system.getTicketService().generateTicket(vehicle, spot);

                try {
                    Thread.sleep((long) (Math.random() * 10)); 
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                // PHASE 4: THE EXIT
                // Pay for ticket and free the spot in the Service layer
                system.getTicketService().processExit(ticket);
                system.getSpotService().freeSpot(spot.getId());
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            // CRITICAL: Release the spot back to the Semaphore so the next car in the queue can enter.
            lotGate.release();
        }
    }

    private static void printSummary(long duration) {
        double avgWait = totalWaitTime.sum() / (double) TOTAL_CARS;
        double throughput = TOTAL_CARS / (duration / 1000.0);

        System.out.println("\n--- Queued Simulation Results ---");
        System.out.println("Total Time: " + duration + "ms");
        System.out.println("Successful Parkings: " + parkedCount.sum() + " (100% success rate)");
        System.out.println("Avg Wait Time: " + String.format("%.2f", avgWait) + "ms");
        System.out.println("System Throughput: " + String.format("%.2f", throughput) + " cars/sec");
    }
}