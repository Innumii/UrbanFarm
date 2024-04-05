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
    private final int GROWTH_TIME = 5000; // time taken to reach a new growth stage, change if needed
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

    // creates an instance of a plant with its onClick events
    private void init() {
        Log.d(TAG, "Initializing PlantSlot with dirt image.");
        setImageResource(R.drawable.dirt); // a plant slot is empty by default
        setScaleType(ScaleType.FIT_CENTER);
        setOnClickListener(v -> {
            if(growthStage == 0) {
                grow(); // sow a plant on an empty slot
            } else if (growthStage == 3) {
                harvest(); // a fully grown plant can be harvested
            }
        });
    }

    private void grow() {
        growthStage++;
        updatePlantImage();
        growRunnable = new Runnable() {
            @Override
            public void run() {
                // while a plant is not fully grown, keep increasing its growth stage
                if(growthStage < 3) {
                    growthStage++;
                    updatePlantImage();
                    handler.postDelayed(this, GROWTH_TIME);
                }
            }
        };
        handler.postDelayed(growRunnable, GROWTH_TIME);
    }

    // harvests the (fully grown) plant and resets it back to an empty slot
    private void harvest() {
        growthStage = 0;
        updatePlantImage();
        handler.removeCallbacks(growRunnable);
        if (harvestListener != null) {
            harvestListener.onHarvest();
        }
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

    public interface OnHarvestListener {
        void onHarvest();
    }

    private OnHarvestListener harvestListener;

    public void setOnHarvestListener(OnHarvestListener harvestListener) {
        this.harvestListener = harvestListener;
    }
}
