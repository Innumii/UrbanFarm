package com.example.cs205_assignment4;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.ViewTreeObserver;
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

    private AnimatedSquareView squareView;
    private TextView squareView2;
    private Handler handler;
    private int currentSize;
//    private int currentSize2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        squareView = findViewById(R.id.squareView);
//        squareView2 = findViewById(R.id.squareView2);
        handler = new Handler(Looper.getMainLooper());

        // Use ViewTreeObserver to wait for layout to be measured
        ViewTreeObserver observer = squareView.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Start increasing size every 5 seconds
                currentSize = squareView.getWidth(); // Get width after layout is measured
                Log.d("TAG", "Message: " + currentSize);
                squareView.startIncreasingSize();
//                startIncreasingSize(squareView, currentSize);
                squareView.getViewTreeObserver().removeOnGlobalLayoutListener(this); // Remove listener after use
            }
        });

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
    }

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

}