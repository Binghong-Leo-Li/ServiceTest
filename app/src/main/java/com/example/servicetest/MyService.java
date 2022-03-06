package com.example.servicetest;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;


public class MyService extends Service {

    // Creating Class to receiver of request to know service status
    private class ServiceEchoReceiver extends BroadcastReceiver {
        public void onReceive (Context context, Intent intent) {
            LocalBroadcastManager
                    .getInstance(context)
                    .sendBroadcastSync(new Intent("pong"));
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Broadcast for start
        LocalBroadcastManager
                .getInstance(this)
                .registerReceiver(new ServiceEchoReceiver(), new IntentFilter("ping"));

        // Creating notification channel to enable the notification [ to keep the service running]
        createNotificationChannel();
        Intent intent1 = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent1, 0);
        Notification notification = new NotificationCompat.Builder(this, "ChannelId1")
                .setContentTitle("My Service")
                .setContentText("Service running")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent).build();

        // Configure Foreground service with id and notification
        startForeground(1,notification);

        // Make sure the service itself is sticky
        return START_STICKY;
    }

    private void createNotificationChannel() {
        // Check Build version to know which path to go about initializing the service
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    "ChannelId1", "Foreground notification", NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);
        }
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
        stopSelf();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
