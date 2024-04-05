package com.example.cs205_assignment4;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class FoodStoresMeter {
    private final ProgressBar foodStoresMeter;
    private TextView foodStoresView;
    private final Handler handler;
    private final Context context;
    private int foodStores = 20; // start with some food
    private final int MAX_CAPACITY = 50; // maximum capacity
    private final int CONSUMPTION_RATE = 1000; // in milliseconds
    private final Object lock = new Object(); // lock object for synchronization
    private boolean isConsuming = true;
    private final int MAX_WIDTH = 300;

    public FoodStoresMeter(Context context, Handler handler) {
        this.foodStoresMeter = ((Activity)context).findViewById(R.id.foodStoresMeter);
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
        int progress = (foodStores * 100) / MAX_CAPACITY;
        handler.post(() -> foodStoresMeter.setProgress(progress));
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
                    Thread.sleep(CONSUMPTION_RATE);
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
