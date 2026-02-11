package com.parking;

import java.time.LocalDateTime;

public class Ticket {
    private final String ticketNumber;
    private final Vehicle vehicle;
    private final LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private double amount;
    private TicketStatus status;

    public Ticket(String ticketNumber, Vehicle vehicle) {
        this.ticketNumber = ticketNumber;
        this.vehicle = vehicle;
        this.entryTime = LocalDateTime.now();
        this.status = TicketStatus.ACTIVE;
    }

    // Getters
    public String getTicketNumber() { return ticketNumber; }
    public Vehicle getVehicle() { return vehicle; }
    public LocalDateTime getEntryTime() { return entryTime; }
    public LocalDateTime getExitTime() { return exitTime; }
    public double getAmount() { return amount; }
    public TicketStatus getStatus() { return status; }

    // Setters for lifecycle changes
    public void setExitTime(LocalDateTime exitTime) {
        this.exitTime = exitTime;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }
}