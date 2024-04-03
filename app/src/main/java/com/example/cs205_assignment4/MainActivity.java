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
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.EdgeToEdge;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private SunMoon sunMoon;
    private AnimatedSquareView squareView;
    private TextView dayNightTextView;
    private TextView batteryView;
    private View dayNight; // Corrected variable declaration
    private EnergyLevelView energyLevelView;
    private AnimatedSquareView squareAnimated;
    private TextView squareTextView;
    private boolean isSquare = true;
    private int currentSize;
    private ImageView imageView;
    // Replace with your image IDs
    private int[] imageIds = {R.drawable.sprout, R.drawable.big_plant216x216};
    private int currentIndex = 0;
    private Handler handler;
    private Runnable runnable;
    private FoodStoresMeter foodStoresMeter;
    private TextView foodStoresTextView, maxFoodStoresTextView;
    private Button addFoodButton;
    private final int MAX_FOOD_STORES = 100; // Maximum limit of food stores
    private static final String CHANNEL_ID = "Channel ID1";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Handler uiHandler = new Handler(Looper.getMainLooper());
        foodStoresTextView = findViewById(R.id.foodStoresTextView);
        addFoodButton = findViewById(R.id.addFoodButton);

        foodStoresMeter = new FoodStoresMeter(foodStoresTextView, MAX_FOOD_STORES, this, uiHandler);
        setupButtonListeners();


        dayNight = findViewById(R.id.dayNight);
        dayNightTextView = findViewById(R.id.dayNightTextView); // Assuming it's a TextView displaying "Day" or "Night"
        batteryView = findViewById(R.id.batteryView);
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
                        batteryView.setText("Battery : " + (Math.round(battery.getEnergyStored() * 100.0) / 100.0) + " / " + battery.getCapacity());
                        // Update dayNightTextView text based on time of day
                        dayNightTextView.setText(isDay ? "Day : " + timeOfDay: "Night : " + timeOfDay);
                    }
                });
            }
        });
        sunMoon.startDayNightCycle();


        squareAnimated = findViewById(R.id.squareView);
        // Use ViewTreeObserver to wait for layout to be measured
        ViewTreeObserver observer = squareAnimated.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Start increasing size every 5 seconds
                currentSize = squareAnimated.getWidth(); // Get width after layout is measured
                squareAnimated.startIncreasingSize();
                squareAnimated.getViewTreeObserver().removeOnGlobalLayoutListener(this); // Remove listener after use
            }
        });

        squareTextView = findViewById(R.id.squareTextView);
        Button changeShapeButton = findViewById(R.id.changeShapeButton);
        changeShapeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSquare) {
                    squareTextView.setBackgroundResource(R.drawable.circle_shape); // Change to circle shape
                } else {
                    squareTextView.setBackgroundResource(R.drawable.custom_shapes); // Change back to square shape
                }
                isSquare = !isSquare; // Toggle the flag for the next click
            }
        });

        imageView = findViewById(R.id.imageView);
        Handler handlerPlant = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                // Set the next image in the array
                imageView.setImageResource(imageIds[currentIndex]);
                currentIndex = (currentIndex + 1) % imageIds.length; // Rotate through images
                handlerPlant.postDelayed(this, 3000); // Repeat every 3000 milliseconds (3 seconds)
            }
        };

        handler.post(runnable); // Start the initial image rotation

        Button notiButton = findViewById(R.id.notiButton);
        notiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNotification();
            }
        });
    }

    private void addNotification() {
            try {
//                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
//                builder.setSmallIcon(R.mipmap.ic_launcher_round)
//                        .setContentTitle("My notification")
//                        .setContentText("ding ding")
//                        .setAutoCancel(true)
//                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//
//                //create intent to show noti
//                Intent notiIntent = new Intent(getApplicationContext(), MainActivity.class);
//                notiIntent.addFlags(notiIntent.FLAG_ACTIVITY_CLEAR_TOP);
//                notiIntent.putExtra("data", "some info here");
//                PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, notiIntent,
//                        PendingIntent.FLAG_MUTABLE);
//                builder.setContentIntent(contentIntent);
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    NotificationChannel channel = manager.getNotificationChannel(CHANNEL_ID);
//                    if (channel == null) {
//                        int importance = NotificationManager.IMPORTANCE_HIGH;
//                        channel = new NotificationChannel(CHANNEL_ID, "DESCRIPTION", importance);
//                        channel.setLightColor(Color.GREEN);
//                        manager.createNotificationChannel(channel);
//                    }
//                }
//                manager.notify(0, builder.build());

//                if (ContextCompat.checkSelfPermission(MainActivity.this, permi))
            } catch (Exception e) {
                System.out.println("Error123: " +  e);
            }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove the callback to prevent memory leaks
        handler.removeCallbacks(runnable);
        Handler uiHandler = new Handler(Looper.getMainLooper());
        foodStoresTextView = findViewById(R.id.foodStoresTextView);
        addFoodButton = findViewById(R.id.addFoodButton);

        foodStoresMeter = new FoodStoresMeter(foodStoresTextView, MAX_FOOD_STORES, this, uiHandler);
        setupButtonListeners();
    }

    private void setupButtonListeners() {
        addFoodButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                foodStoresMeter.increaseFoodStores(10);
            }
        });
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


//        ViewTreeObserver observer2 = squareView2.getViewTreeObserver();
//        observer2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                currentSize2 = squareView2.getWidth(); // Get width after layout is measured for squareView2
//                startIncreasingSize(squareView2, currentSize2);
//                squareView2.getViewTreeObserver().removeOnGlobalLayoutListener(this); // Remove listener after use
//            }
//        });


//
//        Button btnClickMe = findViewById(R.id.buttonClick);
//        btnClickMe.setOnClickListener(view -> {
//            showAlertDialog("You get scammed!");
//        });
//
//
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

//    private void showAlertDialog(String message) {
//        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
//                .setTitle("AUGGIE")
//                .setMessage(message)
//                .setPositiveButton("KK, im a veggie", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                }).create();
//        alertDialog.show();
//    }