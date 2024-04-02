package com.example.cs205_assignment4;

public class Battery {
    private final double capacity; // Capacity of the battery in kWh
    private double chargeLevel; // Current charge level of the battery in kWh
    private final double drainRate = 10.0;
    private final SunMoon sunmoon;

    public Battery(double capacity, SunMoon sunmoon) {
        this.capacity = capacity;
        this.chargeLevel = 0; // Battery starts with zero charge
        this.sunmoon = sunmoon;
        useBattery();
    }

    public void useBattery(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(sunmoon.getTimeFactor()); // Update brightness every 100 milliseconds
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (sunmoon.isDay()){
                        drain(drainRate / 4);
                    } else {
                        drain(drainRate);
                    }
                }
            }
        }).start();
    }

    public synchronized void charge(double energy) {
        // Add the collected energy to the battery's charge level
        chargeLevel = Math.min(chargeLevel + energy, capacity);
    }

    public synchronized void drain(double energy) {
        // Add the collected energy to the battery's charge level
        chargeLevel = Math.max(chargeLevel - energy, 0);
    }

    public double getCapacity() {
        return capacity;
    }

    // Method to retrieve the stored energy level
    public double getEnergyStored() {
        return chargeLevel;
    }
}
