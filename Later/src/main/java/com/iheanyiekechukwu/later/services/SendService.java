package com.iheanyiekechukwu.later.services;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;

import java.util.Calendar;

/**
 * Created by iekechuk on 12/3/13.
 */
public class SendService extends Service {
    private final IBinder mBinder  = new SendBinder();

    public static final String REMOVE_LATEST = "com.iheanyiekechukwu.later.REMOVE";
    private final Handler handler = new Handler();
    Intent intent;
    private NotificationManager nm;

    private int NOTIFICATION_ID = 0;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //intent.getArray
        //Toast.makeText(getApplicationContext(), "Service Running", Toast.LENGTH_SHORT).show();
        //getApplicationContext().


//        getApplicationContext().run
        //return Service.START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        intent = new Intent(REMOVE_LATEST);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        handler.removeCallbacks(sendUpdatesToUI);
        handler.postDelayed(sendUpdatesToUI, 1000); // 1 second
    }

    private void checkLatest() {
        Calendar current = Calendar.getInstance();
        intent.putExtra("date", current);
        sendBroadcast(intent);

    }
    private Runnable sendUpdatesToUI = new Runnable() {
        public void run() {
            //DisplayLoggingInfo();

            //Toast.makeText(getBaseContext(), "Testing out things!", Toast.LENGTH_SHORT).show();
            checkLatest();
            handler.postDelayed(this, 1000*5); // 5 seconds
        }
    };

    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class SendBinder extends Binder {
        public SendService getService() {
            return SendService.this;
        }
    }
}
