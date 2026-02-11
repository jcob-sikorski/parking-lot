# Parking Lot Management System

The system is built using solid software design principles, incorporating **Strategy**, **Observer**, and **Singleton** patterns.

## Project Goal: High-Throughput Simulation

The primary objective of this project is to engineer a system capable of simulating extreme traffic loads. It is designed to efficiently handle **100,000 to 1,000,000 vehicles per second** processing through entry and exit gate terminals. This stress-tests the system's concurrency models and throughput efficiency, ensuring it can scale to massive real-world or simulated demands without bottlenecks.

## Key Features & Requirements

The system satisfies the following core requirements:

* **Multi-Level Architecture:** Supports a parking garage with multiple floors (`Level`), each containing a specific inventory of spots.
* **Vehicle & Spot Typing:**
* **Vehicles:** Distinct classes for `Car`, and `Truck`.
* **Spots:** Specialized `ParkingSpot` types (`COMPACT`, `LARGE`) to ensure large vehicles don't occupy small spots.


* **Automated Gate Management:**
* **Entry Terminals:** Automatically assign spots via `SpotService` and issue timestamped tickets via `TicketService`.
* **Exit Terminals:** Calculate fees based on duration and strategy, process payment, and release spots back to the pool.


* **Dynamic Pricing:** Uses the **Strategy Pattern** to easily switch between billing logic (e.g., `FixedFee` vs. `ProgressiveFee`) without changing the core system.
* **Real-time Availability:** Uses the **Observer Pattern** (`NotificationService`) to update `DisplayBoards` instantly when a car enters or leaves.
* **Concurrency:** Thread-safe implementation prevents "double-booking" race conditions when multiple vehicles enter simultaneously.

<img src="https://ucarecdn.com/c743c438-312e-40d6-b03b-d864e1504b64/UML.png" alt="Parking Lot System UML" width="800">

## Core Components

### 1. Gate Management (Terminals)

* **`EntryTerminal`:** Handles the vehicle entry workflow. It requests a spot from the `SpotService` and issues a `Ticket` via the `TicketService`.
* **`ExitTerminal`:** Handles the exit workflow. It scans a `Ticket`, calculates the final fee using the active `PricingStrategy`, and releases the spot.

### 2. Services (The Business Logic)

* **`SpotService`:** The brain of the parking allocation. It holds the `SpotSearchStrategy` (e.g., `NearestFit` or `OptimalFit`) to find the best spot for a specific vehicle type.
* **`TicketService`:** Manages the lifecycle of tickets and links them to specific vehicles and entry times.
* **`NotificationService`:** Acts as the subject in the Observer pattern, notifying subscribers (like display boards) whenever the parking state changes.

### 3. Domain Models

* **`ParkingLot` (Singleton):** The central entry point that initializes and holds references to all services.
* **`Level` & `ParkingSpot`:** Represents the physical layout. Spots track their own availability and type.
* **`Vehicle`:** Abstract base class with concrete implementations for `Car`, `Truck`, etc.

## Design Patterns Applied

| Pattern | Usage | Benefit |
| --- | --- | --- |
| **Singleton** | `ParkingLot` class | Ensures a single, shared state for the entire garage inventory. |
| **Strategy** | `PricingStrategy` (`Fixed`, `Progressive`) | Allows changing fee logic (e.g., weekend rates vs. hourly) without modifying the `TicketService`. |
| **Strategy** | `SpotSearchStrategy` (`Nearest`, `Optimal`) | Enables different parking algorithms (e.g., fill ground floor first vs. save fuel). |
| **Observer** | `NotificationService` & `DisplayBoard` | Decouples the display logic from the parking logic; screens update automatically on state changes. |
| **Factory/Abstract** | `Vehicle` & `Gate` | Simplifies the creation and management of different vehicle and terminal types. |