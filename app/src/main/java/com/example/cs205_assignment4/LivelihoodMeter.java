package com.example.cs205_assignment4;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.widget.ProgressBar;

public class LivelihoodMeter {
    private final ProgressBar livelihoodMeter;
    private final Handler handler;
    private final Context context;
    private int livelihood = 100;
    private final int MAX_LIVELIHOOD = 100;
    private final int MIN_LIVELIHOOD = 0;
    private final Object lock = new Object();
    private boolean isActive = true;

    public LivelihoodMeter(Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
        this.livelihoodMeter = ((Activity)context).findViewById(R.id.livelihoodMeter);
    }

    public void decreaseLivelihood(int amount) {
        if(!isActive) {
            return;
        }
        synchronized (lock) {
            livelihood = Math.max(MIN_LIVELIHOOD, livelihood - amount);
            updateDisplay();

            if(livelihood <= 0) {
                stop();
                gameOver();
            }
        }
    }

    public void increaseLivelihood(int amount) {
        if(!isActive) {
            return;
        }
        synchronized (lock) {
            livelihood = Math.min(MAX_LIVELIHOOD, livelihood + amount);
            updateDisplay();
        }
    }

    private void gameOver() {
        handler.post(() -> {
            if (context instanceof MainActivity) {
                ((MainActivity) context).showGameOverScreen();
            }
        });
    }

    private void updateDisplay() {
        int progress = (livelihood * 100) / MAX_LIVELIHOOD;
        handler.post(() -> livelihoodMeter.setProgress(progress, true));
    }

    private void stop() {
        isActive = false;
    }
}
