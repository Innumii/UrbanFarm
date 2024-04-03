package com.example.cs205_assignment4;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class EnergyLevelView extends View implements Battery.BatteryListener {
    private Paint paint;
    private double batteryCapacity;
    private double energyLevel;

    public EnergyLevelView(Context context) {
        super(context);
        init();
    }

    public EnergyLevelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EnergyLevelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.YELLOW); // Change color as needed
        paint.setStyle(Paint.Style.FILL);
    }

    public void setBattery(Battery battery) {
        battery.addListener(this); // Listen for changes in battery level
        batteryCapacity = battery.getCapacity();
        energyLevel = battery.getEnergyStored() / batteryCapacity;
        invalidate(); // Request redraw of the view
    }

    @Override
    public void onBatteryLevelChanged(double level) {
        energyLevel = level;
        postInvalidate(); // Request redraw of the view on the UI thread
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Calculate the width of the view based on energy level and battery capacity
        float width = getWidth() * (float) energyLevel;

        // Draw the yellow rectangle representing energy level
        paint.setColor(Color.YELLOW); // Ensure color is set to yellow
        canvas.drawRect(0, 0, width, getHeight(), paint);
    }
}
