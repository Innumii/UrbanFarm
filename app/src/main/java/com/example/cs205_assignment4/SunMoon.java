package com.example.cs205_assignment4;

import android.os.Handler;
import android.os.Looper;

public class SunMoon {
    private boolean isDay = true;
    private Handler handler;

    public SunMoon() {
        handler = new Handler(Looper.getMainLooper());
    }

    public interface DayNightListener {
        void onDay();

        void onNight();
    }

    private DayNightListener dayNightListener;

    public void setDayNightListener(DayNightListener listener) {
        this.dayNightListener = listener;
    }

    public void startDayNightCycle() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000); // Sleep for 20 minutes
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    isDay = !isDay;

                    // Post UI update on main thread
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (isDay) {
                                if (dayNightListener != null) {
                                    dayNightListener.onDay();
                                }
                            } else {
                                if (dayNightListener != null) {
                                    dayNightListener.onNight();
                                }
                            }
                        }
                    });
                }
            }
        }).start();
    }
}
