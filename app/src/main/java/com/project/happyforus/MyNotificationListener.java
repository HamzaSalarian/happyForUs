package com.project.happyforus;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyNotificationListener extends NotificationListenerService {

    private static final String TAG = "MyNotificationListener";
    private static final String CHANNEL_ID = "notification_listener_service_channel";

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        String packageName = sbn.getPackageName();

        // Check if the notification is from a monitored app
        List<ApplicationInfo> monitoredApps = AppDataManager.getInstance().getMonitoredApps();
        boolean isMonitoredApp = false;

        for (ApplicationInfo appInfo : monitoredApps) {
            if (appInfo.packageName.equals(packageName)) {
                isMonitoredApp = true;
                break;
            }
        }

        if (isMonitoredApp) {
            Notification notification = sbn.getNotification();
            String notificationTitle = notification.extras.getString(Notification.EXTRA_TITLE);
            String notificationText = notification.extras.getString(Notification.EXTRA_TEXT);

            Log.d(TAG, "Notification received from: " + packageName);
            Log.d(TAG, "Title: " + notificationTitle);
            Log.d(TAG, "Text: " + notificationText);

            // Create a NotificationData object
            NotificationItem notificationData = new NotificationItem(packageName, notificationTitle, notificationText);

            // Send the notification data to the server using Retrofit
            ApiService apiService = RetrofitClient.getClient("http://192.168.100.21/").create(ApiService.class);
            Call<Void> call = apiService.sendNotificationToServer(notificationData);

            // Handle the response from the server
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "Notification successfully sent to server.");
                    } else {
                        Log.e(TAG, "Failed to send notification to server. Response code: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e(TAG, "Error sending notification to server: " + t.getMessage());
                }
            });
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.d(TAG, "Notification removed from: " + sbn.getPackageName());
    }



    public void onListenerConnected() {
        Log.d(TAG, "NotificationListenerService connected");
    }

    @Override
    public void onListenerDisconnected() {
        Log.d(TAG, "NotificationListenerService disconnected");

        // Send a notification to the user informing that the listener has stopped
        sendListenerStoppedNotification();
        EmailSender emailSender = new EmailSender();
        emailSender.sendEmail();
    }

    private void sendListenerStoppedNotification() {
        createNotificationChannel();

        Log.d(TAG, "sendListenerStoppedNotification: Called");
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground) // Replace with your app's icon
                .setContentTitle("Listener Service Stopped")
                .setContentText("Notification listener service has stopped unexpectedly.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();

        notificationManager.notify(1, notification);  // ID 1 for this notification
    }



    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notification Listener Service Channel";
            String description = "Channel for notification listener service";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }


    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.d(TAG, "App removed from the background");

        // Send a notification here
        sendListenerStoppedNotification();

        // Optionally restart the service if needed
        super.onTaskRemoved(rootIntent);
    }

}
