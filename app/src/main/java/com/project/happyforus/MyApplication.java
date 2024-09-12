package com.project.happyforus;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class MyApplication extends Application {

    private static final String CRASH_CHANNEL_ID = "crash_notification_channel";
    private static final int CRASH_NOTIFICATION_ID = 1;

    @Override
    public void onCreate() {
        super.onCreate();

        // Set a default uncaught exception handler
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            Log.e("GlobalException", "Uncaught exception: " + throwable.getMessage(), throwable);

            // Send crash notification
            sendCrashNotification();

            // Pass the exception to the default system handler (optional)
            System.exit(1); // Exit after handling the exception
        });

        // Create notification channel for crash notifications
        createCrashNotificationChannel();
    }

    private void sendCrashNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = new NotificationCompat.Builder(this, CRASH_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground) // Use your app icon here
                .setContentTitle("App Crashed")
                .setContentText("The app has crashed unexpectedly. Please restart it.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .build();

        notificationManager.notify(CRASH_NOTIFICATION_ID, notification);
    }

    private void createCrashNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name = "Crash Notification Channel";
            String description = "Channel for crash notifications";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CRASH_CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
}
