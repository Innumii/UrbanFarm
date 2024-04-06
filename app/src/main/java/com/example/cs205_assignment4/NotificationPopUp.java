package com.example.cs205_assignment4;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationPopUp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        notificationChannel();
    }

    public static final String CHANNEL_ID = "channelID";
    public static final String CHANNEL_NAME = "channelName";
    public static final String CHANNEL_DESC = "channelDesc";

    private void notificationChannel() {
        System.out.println("TEST TEST");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(CHANNEL_DESC);

            NotificationManager managerCompat = getSystemService(NotificationManager.class);
            managerCompat.createNotificationChannel(channel);
        }

    }
}

