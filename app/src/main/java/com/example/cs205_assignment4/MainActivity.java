package com.example.cs205_assignment4;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.EdgeToEdge;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PlantSlot.OnHarvestListener {
    private SunMoon sunMoon;
    private TextView dayNightTextView, foodStoresTextView;
    private View dayNight;
    private EnergyLevelView energyLevelView;
    private Handler handler;
    private Runnable runnable;
    private FoodStoresMeter foodStoresMeter;
//    private Button addFoodButton;
    private final int FOOD_AMOUNT = 15; // each harvest increases food stores by this amount
    private static final String CHANNEL_ID = "Channel ID1";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Handler uiHandler = new Handler(Looper.getMainLooper());

        foodStoresMeter = new FoodStoresMeter(this, uiHandler);

        dayNight = findViewById(R.id.dayNight);
        dayNightTextView = findViewById(R.id.dayNightTextView);
        Handler handler = new Handler(Looper.getMainLooper());

        // Start the day-night cycle simulation
        SunMoon sunMoon = new SunMoon();
        int battery_capacity = 250;
        Battery battery = new Battery(battery_capacity, sunMoon);
        List<SolarPanel> solarPanels = new ArrayList<>();

        int numberOfSolarPanels = 2;
        for (int i = 0; i < numberOfSolarPanels; i++) {
            SolarPanel solarPanel = new SolarPanel(sunMoon, battery);
            solarPanels.add(solarPanel);
            new Thread(solarPanel).start();
        }

        energyLevelView = findViewById(R.id.energyLevelView);
        energyLevelView.setBattery(battery);

        sunMoon.setDayNightListener(new SunMoon.DayNightListener() {
            @Override
            public void onTransition(float brightness, boolean isDay, String timeOfDay) {
                // Update UI for smooth transition
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Update squareView background color based on brightness
                        int backgroundColor = calculateBackgroundColor(brightness);
                        dayNight.setBackgroundColor(backgroundColor);
//                        batteryView.setText("Battery : " + (Math.round(battery.getEnergyStored() * 100.0) / 100.0) + " / " + battery.getCapacity());
                        // Update dayNightTextView text based on time of day
                        dayNightTextView.setText(isDay ? "Day : " + timeOfDay: "Night : " + timeOfDay);
                    }
                });
            }
        });
        sunMoon.startDayNightCycle();

        // setting a grid for the planting slots
        GridLayout plantSlotsGridLayout = findViewById(R.id.plantSlotsGridLayout);
        int totalSlots = 12; // can change where needed
        int numColumns = plantSlotsGridLayout.getColumnCount();
        int numRows = totalSlots / numColumns;
        plantSlotsGridLayout.setRowCount(numRows);
        plantSlotsGridLayout.setColumnCount(numColumns);

        for(int i = 0; i < totalSlots; i++) {
            PlantSlot plantSlot = new PlantSlot(this); // initializes a new planting slot
            plantSlot.setOnHarvestListener(this); // tracks user harvesting fully grown plants
            // creates the grid for plant slots
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0;
            params.height = 0;
            params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1, 1f);
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1, 1f);
            plantSlot.setLayoutParams(params);
            plantSlotsGridLayout.addView(plantSlot);
        }

        // listening to battery and day/night changes
        battery.addListener(level -> {
            boolean canGrow = level > 0 || sunMoon.isDay();
            for (int i = 0; i < plantSlotsGridLayout.getChildCount(); i++) {
                View view = plantSlotsGridLayout.getChildAt(i);
                if (view instanceof PlantSlot) {
                    ((PlantSlot) view).setGrowthCondition(canGrow);
                }
            }
        });

        sunMoon.setDayNightListener(new SunMoon.DayNightListener() {
            @Override
            public void onTransition(float brightness, boolean isDay, String timeOfDay) {
                boolean canGrow = isDay || (!isDay && battery.getEnergyStored() > 0);
                for (int i = 0; i < plantSlotsGridLayout.getChildCount(); i++) {
                    View view = plantSlotsGridLayout.getChildAt(i);
                    if (view instanceof PlantSlot) {
                        ((PlantSlot) view).setGrowthCondition(canGrow);
                    }
                }

                int backgroundColor = calculateBackgroundColor(brightness);
                dayNight.setBackgroundColor(backgroundColor);
                dayNightTextView.setText(isDay ? "Day : " + timeOfDay : "Night : " + timeOfDay);
            }
        });
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove the callback to prevent memory leaks
        handler.removeCallbacks(runnable);
        Handler uiHandler = new Handler(Looper.getMainLooper());

        foodStoresMeter = new FoodStoresMeter(this, uiHandler);
    }

    // method needed for OnHarvestListener interface created in PlantSlot class
    @Override
    public void onHarvest() {
        foodStoresMeter.increaseFoodStores(FOOD_AMOUNT);
    }

    private int calculateBackgroundColor(float brightness) {
        int dayColor = getResources().getColor(R.color.dayColor);
        int nightColor = getResources().getColor(R.color.nightColor);

        // Interpolate colors based on brightness
        int red = (int) (Color.red(dayColor) * brightness + Color.red(nightColor) * (1 - brightness));
        int green = (int) (Color.green(dayColor) * brightness + Color.green(nightColor) * (1 - brightness));
        int blue = (int) (Color.blue(dayColor) * brightness + Color.blue(nightColor) * (1 - brightness));

        // Ensure that RGB values stay within the valid range
        red = Math.max(0, Math.min(255, red));
        green = Math.max(0, Math.min(255, green));
        blue = Math.max(0, Math.min(255, blue));

        // Return the interpolated color
        return Color.rgb(red, green, blue);
    }

}
