package com.example.cs205_assignment4;

import android.content.Context;
import android.os.Handler;
import android.widget.TextView;

public class FoodStoresMeter {
    private TextView foodStoresView;
    private Handler handler;
    private Context context;
    private int foodStores = 0; // start with empty food stores
    private final int MAX_CAPACITY = 30; // change this number if needed
    private final Object lock = new Object(); // lock object for synchronization

    public FoodStoresMeter(TextView foodStoresView, int MAX_FOOD_STORES, Context context, Handler handler) {
        this.foodStoresView = foodStoresView;
        this.context = context;
        this.handler = handler;
        updateDisplay();
    }

    public void increaseFoodStores(int amount) {
        synchronized(lock) {
            if(foodStores + amount <= MAX_CAPACITY) {
                foodStores += amount;
                handler.post(this::updateDisplay);
            }
        }
    }

    private void updateDisplay() {
        foodStoresView.setText(String.valueOf(foodStores));
    }

    private void consumeFoodStores(int amount) {
        //oh noooo the citizens are consuming the food :o
        new Thread(() -> {
            synchronized (lock) {
                while(foodStores - amount < 0) {
                    try {
                        lock.wait();
                    } catch(InterruptedException e) {
                        e.printStackTrace();
                        return;
                    }
                }
                foodStores -= amount;
                handler.post(this::updateDisplay);
                lock.notifyAll();
            }
        }).start();
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
