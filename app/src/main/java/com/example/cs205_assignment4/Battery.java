package com.example.cs205_assignment4;

public class Battery {
    private double capacity; // Capacity of the battery in kWh
    private double chargeLevel; // Current charge level of the battery in kWh

    public Battery(double capacity) {
        this.capacity = capacity;
        this.chargeLevel = 0; // Battery starts with zero charge
    }

    public synchronized void charge(double energy) {
        // Add the collected energy to the battery's charge level
        chargeLevel = Math.min(chargeLevel + energy, capacity);
    }

    public synchronized double getChargeLevel() {
        return chargeLevel;
    }

    public double getCapacity() {
        return capacity;
    }

    // Method to store energy in the battery
    public void storeEnergy(int energy) {
        chargeLevel += energy;
    }

    // Method to retrieve the stored energy level
    public double getEnergyStored() {
        return chargeLevel;
    }
}
