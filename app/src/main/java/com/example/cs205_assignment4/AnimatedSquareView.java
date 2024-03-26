package com.example.cs205_assignment4;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class AnimatedSquareView extends View {
    private int currentSize;
    private Handler handler;

    public AnimatedSquareView(Context context) {
        super(context);
        init();
    }

    public AnimatedSquareView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AnimatedSquareView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        currentSize = 0;
        handler = new Handler(Looper.getMainLooper());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Draw the square with the current size
        canvas.drawRect(0, 0, currentSize, currentSize, new Paint());
    }

    public void startIncreasingSize() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = currentSize; i < 500; i += 10) {
                    try {
                        Thread.sleep(200); // Sleep for 200 milliseconds between size increments
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    final int finalSize = i; // Store the final size for UI update on the main thread
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            currentSize = finalSize;
                            invalidate(); // Trigger a redraw on each frame
                        }
                    });
                }
            }
        }).start();
    }
}
