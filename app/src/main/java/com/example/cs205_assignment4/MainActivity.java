package com.example.cs205_assignment4;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private TextView squareView;
    private TextView squareView2;
    private Handler handler;
    private int currentSize;
    private int currentSize2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        squareView = findViewById(R.id.squareView);
        squareView2 = findViewById(R.id.squareView2);
        handler = new Handler(Looper.getMainLooper());

        // Start increasing size every 5 seconds
        currentSize = squareView.getWidth(); // Assuming square
        startIncreasingSize();

        currentSize2 = squareView2.getWidth(); // Assuming square
        startIncreasingSize2();


        Button btnClickMe = findViewById(R.id.buttonClick);
        btnClickMe.setOnClickListener(view -> {
            showAlertDialog("You get scammed!");
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void startIncreasingSize() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (currentSize < 500) {
                    currentSize += 10; // Increase size by 10 units
                    squareView.setWidth(currentSize);
                    squareView.setHeight(currentSize);
                    startIncreasingSize(); // Call recursively for next iteration
                }
            }
        }, 5000); // 5000 milliseconds = 5 seconds
    }
    private void startIncreasingSize2() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (currentSize2 < 500) {
                    currentSize2 += 10; // Increase size by 10 units
                    squareView2.setWidth(currentSize2);
                    squareView2.setHeight(currentSize2);
                    startIncreasingSize2(); // Call recursively for next iteration
                }
            }
        }, 5000); // 5000 milliseconds = 5 seconds
    }

    private void showAlertDialog(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle("AUGGIE")
                .setMessage(message)
                .setPositiveButton("KK, im a veggie", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        alertDialog.show();
    }

}