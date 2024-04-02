package com.example.cs205_assignment4;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.widget.TextView;

public class FoodStoresMeter {
    private TextView foodStoresView;
    private Handler handler;
    private Context context;
    private int foodStores = 0; // start with empty food stores
    private final int MAX_CAPACITY = 30; // change this number if needed
    private int consumptionRate = 1500; // in milliseconds
    private final Object lock = new Object(); // lock object for synchronization
    private boolean isConsuming = true;

    public FoodStoresMeter(TextView foodStoresView, int MAX_FOOD_STORES, Context context, Handler handler) {
        this.foodStoresView = foodStoresView;
        this.context = context;
        this.handler = handler;
        updateDisplay();
        consumeFoodStores();
    }

    public void increaseFoodStores(int amount) {
        synchronized(lock) {
            if(foodStores + amount <= MAX_CAPACITY) {
                foodStores += amount;
            } else {
                foodStores = MAX_CAPACITY;
            }
            handler.post(this::updateDisplay);
        }
    }

    private void updateDisplay() {
        foodStoresView.setText(String.valueOf(foodStores));
        foodStoresView.setTextColor(Color.RED);
    }

    private void consumeFoodStores() {
        //oh noooo the citizens are consuming the food :o
        Thread consumptionThread = new Thread(() -> {
            while(isConsuming) {
                synchronized (lock) {
                    if(foodStores > 0) {
                        foodStores--;
                        handler.post(this::updateDisplay);
                    }
                    lock.notifyAll();
                }

                try {
                    Thread.sleep(consumptionRate);
                } catch(InterruptedException e) {
                    e.printStackTrace();
                    return;
                }
            }
        });
        consumptionThread.start();
    }

    // call this function if needed
    public void stopConsumption() {
        isConsuming = false;
    }

    public void resumeConsumption() {
        isConsuming = true;
    }

    private void updateDisplayUI() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                updateDisplay();
            }
        });
    }
}
