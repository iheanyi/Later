package com.iheanyiekechukwu.later.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.iheanyiekechukwu.later.services.SendService;

/**
 * Created by iekechuk on 12/3/13.
 */
public class StartReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context, SendService.class);
        context.startService(service);
    }
}
