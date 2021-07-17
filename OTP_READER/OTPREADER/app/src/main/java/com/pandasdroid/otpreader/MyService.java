package com.pandasdroid.otpreader;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class MyService extends Service {
    private static final int NOTIF_ID=1;

    @Override
    public void onCreate(){
        this.startForeground();
        registerReceiver(new MessageReceiver(),new IntentFilter("android.provider.Telephony.DATA_SMS_RECEIVED"));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    private void startForeground(){
        startForeground(NOTIF_ID,buildForegroundNotification(""));
    }

    private Notification buildForegroundNotification(@NonNull String action) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
        }
        //Do any customization you want here
        String title;
        /*if (ACTION_STOP.equals(action)) {
            title = getString(R.string.fg_notitifcation_title_stopping);
        } else {
            title = getString(R.string.fg_notitifcation_title_starting);
        }*/
        //then build the notification
        return new NotificationCompat.Builder(this, "not_chanl")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Reading Your Messages")
                .setOngoing(true)
                .build();
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void createNotificationChannel(){
        NotificationChannel chan = new NotificationChannel("not_chanl", "fg_not_channel", NotificationManager.IMPORTANCE_DEFAULT);
        chan.setLightColor(Color.RED);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);
    }

    /**
     * This is the method that can be called to update the Notification
     */
    private void updateNotification(){
        String text="Some text that will update the notification";

        //Notification notification= getMyActivityNotification(text);

        //NotificationManager mNotificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        //mNotificationManager.notify(NOTIF_ID,notification);
    }
}