package com.example.cs205_assignment4;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.widget.TextView;

public class FoodStoresMeter {
    private TextView foodStoresView;
    private Handler handler;
    private Context context;
    private int foodStores = 0; // start with empty food stores
    private final int maxCapacity = 5; // change this number if needed
    private final Object lock = new Object(); // lock object for synchronization

    public FoodStoresMeter(TextView foodStoresView, Context context) {
        this.foodStoresView = foodStoresView;
        this.context = context;
        updateDisplay();
    }

    public void increaseFoodStores(int amount) {
        synchronized(lock) {
            while(foodStores + amount > maxCapacity) {
                try {
                    lock.wait(); // you cannot add to a full buffer
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            foodStores += amount;
            updateDisplay();
            lock.notify();
        }
    }

    private void updateDisplay() {
        foodStoresView.setText(String.valueOf(foodStores));
    }

    private void consumeFoodStores() {
        //oh noooo the citizens are consuming the food :o
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                while(true) {
                    synchronized(lock) {
                        while(foodStores <= 0) {
                            try {
                                lock.wait(); // the citizens cannot consume from empty stores
                            } catch(InterruptedException e) {
                                e.printStackTrace();
                            }
                            consume(1);
                        }
                        try {
                            Thread.sleep(60000); // 1 minute
                        } catch(InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    private void consume(int amount) {
        foodStores -= amount;
        if(foodStores < 0) {
            foodStores = 0;
        }
        updateDisplayUI();
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
