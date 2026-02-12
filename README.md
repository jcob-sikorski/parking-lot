# High-Performance Parking Lot System

> **Validated Throughput:** ~208,000 cars/sec | **Concurrency:** 200,000+ Virtual Threads

This project is a high-performance simulation of a Parking Lot Management System engineered to handle extreme traffic loads. Built with **Java 21**, it leverages **Virtual Threads** and **Lock-Free Concurrency** to simulate millions of vehicles with enterprise-grade throughput, while adhering to solid software design principles like **Strategy**, **Observer**, and **Singleton**.

## 🚀 Performance Benchmarks

The system has been stress-tested on consumer hardware (Apple Silicon) with the following results:

| Metric | Result | Description |
| --- | --- | --- |
| **Peak Throughput** | **208,550 cars/sec** | Achieved during "Burst Mode" simulation. |
| **Concurrency** | **200,000+ Threads** | Concurrent active virtual threads managed via Semaphores. |
| **Queue Buffering** | **34,000+ Cars** | Successfully buffered in memory without `OutOfMemoryError`. |
| **Scalability** | **50 Million Cars** | Validated stability over long-running simulations. |

### Simulation Architecture

* **Producer:** Multi-threaded load generator pushing tasks faster than processing speed.
* **Consumer:** 10,000 parking spots with configurable "stay duration" (1ms - 200ms).
* **Backpressure:** Implemented via `Semaphore(200_000)` to prevent JVM memory starvation.
* **Stats Tracking:** Uses `LongAdder` for high-frequency, lock-free statistical counters.

---

## Project Goal: Extreme Concurrency

The primary objective is to engineer a system capable of simulating **Wait-Free** and **Lock-Free** interactions at scale. Unlike traditional thread-per-request models, this system uses Java's Project Loom (Virtual Threads) to decouple hardware threads from logical tasks, allowing it to scale to massive real-world demands without IO-blocking bottlenecks.

## Key Features & Requirements

### 1. High-Concurrency Core

* **Virtual Threads (Project Loom):** Uses `Executors.newVirtualThreadPerTaskExecutor()` to handle millions of tasks with minimal memory footprint (~1KB per thread).
* **Backpressure Handling:** Uses `Semaphore` to act as an admission controller, preventing the "Thundering Herd" problem by pausing the producer when the system reaches capacity.
* **Thread-Safe Inventory:** Prevents "double-booking" race conditions using atomic primitives and synchronized blocks only where strictly necessary.

### 2. Domain Features

* **Multi-Level Architecture:** Supports a parking garage with multiple floors (`Level`), each containing a specific inventory of spots.
* **Vehicle & Spot Typing:**
* **Vehicles:** Distinct classes for `Car` and `Truck`.
* **Spots:** Specialized `ParkingSpot` types (`COMPACT`, `LARGE`) to ensure large vehicles don't occupy small spots.


* **Automated Gate Management:**
* **Entry Terminals:** Automatically assign spots via `SpotService` and issue timestamped tickets.
* **Exit Terminals:** Calculate fees based on strategy, process payment, and release spots back to the pool.



### 3. Design Patterns

* **Dynamic Pricing (Strategy):** Switch between billing logic (e.g., `FixedFee` vs. `ProgressiveFee`) without changing core code.
* **Real-time Availability (Observer):** `NotificationService` updates `DisplayBoards` instantly when a car enters or leaves.
* **Search Algorithms (Strategy):** Uses `RandomFit` (for performance) or `NearestFit` (for logic) to find spots.

<img src="[https://ucarecdn.com/c743c438-312e-40d6-b03b-d864e1504b64/UML.png](https://ucarecdn.com/c743c438-312e-40d6-b03b-d864e1504b64/UML.png)" alt="Parking Lot System UML" width="800">

## Core Components

### 1. Gate Management (Terminals)

* **`EntryTerminal`:** Handles vehicle entry. It uses a `BlockingQueue` or direct `Semaphore` acquisition to manage entry flow.
* **`ExitTerminal`:** Scans `Ticket`, calculates the final fee using the active `PricingStrategy`, and releases the spot.

### 2. Services (The Business Logic)

* **`SpotService`:** The brain of parking allocation. In high-load scenarios, it uses optimized search strategies to reduce CPU cache contention.
* **`TicketService`:** Manages the lifecycle of tickets, linking them to specific vehicles and entry times.
* **`NotificationService`:** Acts as the subject in the **Observer** pattern, broadcasting state changes to display boards.

### 3. Domain Models

* **`ParkingLot` (Singleton):** The central entry point that initializes and holds references to all services.
* **`Level` & `ParkingSpot`:** Represents the physical layout.
* **`Vehicle`:** Abstract base class with concrete implementations.

## Design Patterns Applied

| Pattern | Component | Benefit |
| --- | --- | --- |
| **Singleton** | `ParkingLot` | Ensures a single, shared state for the entire garage inventory. |
| **Strategy** | `PricingStrategy` | Allows changing fee logic (e.g., Weekend vs. Hourly) dynamically. |
| **Strategy** | `SpotSearchStrategy` | Enables different parking algorithms (`RandomFit` for speed, `NearestFit` for logic). |
| **Observer** | `NotificationService` | Decouples display logic from parking logic; screens update automatically. |
| **Factory** | `Vehicle` & `Terminal` | Simplifies the creation of different vehicle and terminal types. |
| **Producer-Consumer** | `App.java` | Decouples the traffic generation (Producer) from the parking logic (Consumer). |