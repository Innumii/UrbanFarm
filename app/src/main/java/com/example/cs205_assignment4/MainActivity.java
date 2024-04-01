package com.example.cs205_assignment4;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cs205_assignment4.SunMoon;

import androidx.activity.EdgeToEdge;

public class MainActivity extends AppCompatActivity {
    private SunMoon sunMoon;
    private AnimatedSquareView squareView;
    private TextView squareView2;
    private Handler handler;
    private int currentSize;
    private FoodStoresMeter foodStoresMeter;
    private TextView foodStoresTextView, maxFoodStoresTextView;
    private Button addFoodButton;
    private final int MAX_FOOD_STORES = 100; // Maximum limit of food stores

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        squareView = findViewById(R.id.squareView);
        handler = new Handler(Looper.getMainLooper());

        // Start the day-night cycle simulation
        sunMoon = new SunMoon();
        sunMoon.setDayNightListener(new SunMoon.DayNightListener() {
            @Override
            public void onDay() {
                // Update UI for day
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Update UI elements for day
                        TextView dayNightTextView = findViewById(R.id.dayNightTextView);
                        dayNightTextView.setText("Day");
                    }
                });
            }

            @Override
            public void onNight() {
                // Update UI for night
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Update UI elements for night
                        TextView dayNightTextView = findViewById(R.id.dayNightTextView);
                        dayNightTextView.setText("Night");
                    }
                });
            }
        });
        sunMoon.startDayNightCycle();

        // Use ViewTreeObserver to wait for layout to be measured
        ViewTreeObserver observer = squareView.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Start increasing size every 5 seconds
                currentSize = squareView.getWidth(); // Get width after layout is measured
                Log.d("TAG", "Message: " + currentSize);
                squareView.startIncreasingSize();
                squareView.getViewTreeObserver().removeOnGlobalLayoutListener(this); // Remove listener after use
            }
        });

        foodStoresTextView = findViewById(R.id.foodStoresTextView);
        maxFoodStoresTextView = findViewById(R.id.maxFoodStoresTextView);
        addFoodButton = findViewById(R.id.addFoodButton);

        maxFoodStoresTextView.setText("Max food stores capacity: " + MAX_FOOD_STORES);
        foodStoresMeter = new FoodStoresMeter(foodStoresTextView, MAX_FOOD_STORES, this);

        addFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                foodStoresMeter.increaseFoodStores(10);
            }
        });


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