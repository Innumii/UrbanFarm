package com.example.cs205_assignment4;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class NotificationHelper {

    public static final int VARIABLE_NOTIFICATION_ID = 2; // Unique ID for the variable-based notification
    public static final String CHANNEL_ID = "channel_id";
    public static final String CHANNEL_NAME = "Game Notifications";

    public static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    public static void showVariableNotification(Context context, int variableValue) {
        if (variableValue == 0) {
            // Create a PendingIntent for the notification action
            Intent notificationIntent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                    notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            // Build the variable-based notification using NotificationCompat.Builder
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setContentTitle("Your food stores are depleted!")
                    .setContentText("Hurry! Harvest some carrots before your population dies!")
                    .setSmallIcon(R.drawable.logo_1)
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            // Get the NotificationManager system service and show the notification
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.notify(VARIABLE_NOTIFICATION_ID, builder.build());
        }
    }
}
