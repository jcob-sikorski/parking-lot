package com.parking.model.ticket;

import java.time.LocalDateTime;
import java.time.Duration;
import com.parking.model.vehicle.Vehicle;

public class Ticket {
    private final String ticketNumber;
    private final Vehicle vehicle;
    private final LocalDateTime entryTime;
    private String assignedSpotId;
    private LocalDateTime exitTime;
    private double amount;
    private TicketStatus status;

    public Ticket(String ticketNumber, Vehicle vehicle, String assignedSpotId) {
        this.ticketNumber = ticketNumber;
        this.vehicle = vehicle;
        this.assignedSpotId = assignedSpotId;
        this.entryTime = LocalDateTime.now();
        this.status = TicketStatus.ACTIVE;
    }

    public String getAssignedSpotId() {
        return assignedSpotId;
    }

    /**
     * Calculates the hours between entry and exit (or current time if not exited).
     */
    public long getDurationInHours() {
        LocalDateTime end = (exitTime != null) ? exitTime : LocalDateTime.now();
        return Duration.between(entryTime, end).toHours();
    }

    public String getTicketNumber() { return ticketNumber; }
    public Vehicle getVehicle() { return vehicle; }
    public LocalDateTime getEntryTime() { return entryTime; }
    public LocalDateTime getExitTime() { return exitTime; }
    public double getAmount() { return amount; }
    public TicketStatus getStatus() { return status; }

    public void setExitTime(LocalDateTime exitTime) { this.exitTime = exitTime; }
    public void setAmount(double amount) { this.amount = amount; }
    public void setStatus(TicketStatus status) { this.status = status; }
}