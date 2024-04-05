package com.example.cs205_assignment4;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;
import android.util.Log;

public class PlantSlot extends AppCompatImageView {
    private int growthStage = 0; // 0: empty, 1: baby, 2: growing, 3: can harvest
    private final Handler handler = new Handler();
    private Runnable growRunnable;

    public PlantSlot(Context context) {
        super(context);
        init();
    }

    public PlantSlot(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PlantSlot(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Log.d(TAG, "Initializing PlantSlot with dirt image.");
        setImageResource(R.drawable.dirt);
        setScaleType(ScaleType.FIT_CENTER);
        setOnClickListener(v -> {
            if(growthStage == 0) {
                grow();
            } else if (growthStage == 3) {
                harvest();
            }
        });
    }

    private void grow() {
        growthStage++;
        updatePlantImage();
        growRunnable = new Runnable() {
            @Override
            public void run() {
                if(growthStage < 3) {
                    growthStage++;
                    updatePlantImage();
                    handler.postDelayed(this, 5000);
                }
            }
        };
        handler.postDelayed(growRunnable, 5000);
    }

    private void harvest() {
        growthStage = 0;
        updatePlantImage();
        handler.removeCallbacks(growRunnable);
    }

    private void updatePlantImage() {
        switch(growthStage) {
            case 0:
                this.setImageResource(R.drawable.dirt);
                break;
            case 1:
                this.setImageResource(R.drawable.crop_1);
                break;
            case 2:
                this.setImageResource(R.drawable.crop_2);
                break;
            case 3:
                this.setImageResource(R.drawable.crop_3);
                break;
            default:
                break;
        }
    }
}
